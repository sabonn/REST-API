package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.springboot.CustomData.RequestDTO;
import com.example.springboot.CustomData.RequestType;
import com.example.springboot.CustomData.ResponseDTO;
import com.example.springboot.CustomData.Vessel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * This class contains integration tests for the Vessel API.
 * It tests the creation and retrieval of a Vessel object using TestRestTemplate.
 */
@SpringBootTest(
    classes = com.example.springboot.Application.class, // The main application class to initialize the Spring context.
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT // Use a random port for each test to avoid conflicts.
)
@Testcontainers
public class VesselTest {

    // PostgreSQL container instance
    @Container
    private static PostgreSQLContainer<?> postgresContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.5"))
            .withDatabaseName("database")
            .withUsername("my_user")
            .withPassword("my_password");

    // Port number injected by Spring Boot for the test to run on a local server
    @LocalServerPort
    private int port;

    // The TestRestTemplate instance used to make HTTP requests in tests
    @Autowired
    private TestRestTemplate restTemplate;

    // Dynamically set Spring Boot properties using the container's values

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add(
            "spring.datasource.username",
            postgresContainer::getUsername
        );
        registry.add(
            "spring.datasource.password",
            postgresContainer::getPassword
        );
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update"); // Ensure Hibernate updates schema
    }

    // Create the vessel table before tests run
    @BeforeAll
    public static void setUp() throws Exception {
        postgresContainer.start();

        // Create a connection to the PostgreSQL database
        try (
            Connection connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
            )
        ) {
            // SQL statement to create the "vessel" table
            String createTableSQL =
                "CREATE TABLE IF NOT EXISTS vessel (" +
                "id UUID PRIMARY KEY, " +
                "type VARCHAR(255), " +
                "color VARCHAR(255));";

            // SQL statements to insert default data into the "vessel" table
            String insertDataSQL =
                "INSERT INTO vessel (id, type, color) VALUES " +
                "('32c145f4-4898-400e-af9c-f50f4aeab512', 'Cargo', 'Red'), " +
                "('bb39cc00-eff8-4d8a-b2c7-122f31e20a50', 'Passenger', 'Blue'), " +
                "('a8392946-b34f-41e8-b1bf-82cb9c11aacd', 'Military', 'Blue'), " +
                "('37b2a246-cdc3-40c3-b09d-3708299d17b2', 'Fishing', 'Green');";

            // Execute the SQL statements
            try (Statement statement = connection.createStatement()) {
                // Create table
                statement.execute(createTableSQL);

                // Insert default data
                statement.execute(insertDataSQL);
            } catch (Exception e) {
                System.err.println(
                    "FAILED TO EXECUTE SQL STATEMENTS: " + e.getMessage()
                );
            }
        } catch (Exception e) {
            System.err.println(
                "FAILED TO CONNECT TO DATABASE: " + e.getMessage()
            );
        }
    }

    // Clean up after all tests have finished
    @AfterAll
    public static void tearDown() {
        // Stop the PostgreSQL container after all tests
        postgresContainer.stop();
    }

    /**
     * Test for creating a new Vessel.
     * This test sends a POST request to the "/api" endpoint to create a new vessel.
     */
    @Test
    void testCreateVessel() {
        // Create a new Vessel object with a randomly generated UUID, type 'Cargo', and color 'Red'
        Vessel vessel = new Vessel("Cargo", "Red");

        RequestDTO<Vessel> req = new RequestDTO<>(RequestType.CREATE, vessel);

        ResponseEntity<ResponseDTO> response = getResponseEntity(
            req,
            HttpMethod.POST
        );

        // Validate that the response status is HTTP 201 CREATED
        assertEquals(
            HttpStatus.CREATED,
            HttpStatus.valueOf(response.getStatusCode().value())
        );
        Vessel resVessel = (Vessel) response.getBody().getData();
        UUID id = resVessel.getId();
        RequestDTO<UUID> checkReq = new RequestDTO<>(RequestType.GETBYID, id);

        ResponseEntity<ResponseDTO> checkResponse = getResponseEntity(
            checkReq,
            HttpMethod.POST
        );
        Vessel testVessel = (Vessel) checkResponse.getBody().getData();
        assertEquals(resVessel.getColor(), testVessel.getColor());
        assertEquals(resVessel.getType(), testVessel.getType());
        assertEquals(resVessel.getId(), testVessel.getId());
    }

    @Test
    void testGetVesselsByColor() {
        String color = "Blue";
        RequestDTO<String> req = new RequestDTO<>(
            RequestType.GETBYCOLOR,
            color
        );

        // Call the method to get the response
        ResponseEntity<ResponseDTO> response = getResponseEntity(
            req,
            HttpMethod.POST
        );

        // Assert that the response is successful and contains the expected message
        assertEquals(
            HttpStatus.OK,
            HttpStatus.valueOf(response.getStatusCode().value())
        );
        assertEquals("VESSEL'S FOUND", response.getBody().getMessage());
        List<Vessel> actual = (List<Vessel>) response.getBody().getData();

        for (int i = 0; i < actual.size(); i++) {
            Vessel vessel = (Vessel) actual.get(i);
            assertEquals("Blue", vessel.getColor());
        }
    }

    /**
     * Test for retrieving a Vessel by its ID.
     * This test sends a POST request to retrieve a vessel by its UUID.
     */
    @Test
    void testGetVesselById() {
        UUID id = UUID.fromString("32c145f4-4898-400e-af9c-f50f4aeab512");
        RequestDTO<UUID> req = new RequestDTO<>(RequestType.GETBYID, id);

        ResponseEntity<ResponseDTO> response = getResponseEntity(
            req,
            HttpMethod.POST
        );

        assertEquals(
            HttpStatus.OK,
            HttpStatus.valueOf(response.getStatusCode().value())
        );
        assertEquals("VESSEL FOUND", response.getBody().getMessage());
        //'32c145f4-4898-400e-af9c-f50f4aeab512', 'Cargo', 'Red'
        Vessel test = new Vessel(
            UUID.fromString("32c145f4-4898-400e-af9c-f50f4aeab512"),
            "Cargo",
            "Red"
        );
        Vessel actual = (Vessel) response.getBody().getData();
        assertEquals(test.getType(), actual.getType());
        assertEquals(test.getColor(), actual.getColor());
        assertEquals(test.getId(), actual.getId());
    }

    @Test
    void testUpdate() {
        UUID id = UUID.fromString("a8392946-b34f-41e8-b1bf-82cb9c11aacd");
        Vessel vessel = new Vessel(id, "Tanker", "Yellow");

        RequestDTO<Vessel> req = new RequestDTO<>(RequestType.UPDATE, vessel);
        ResponseEntity<ResponseDTO> response = getResponseEntity(
            req,
            HttpMethod.PUT
        );

        // Validate that the response status is HTTP 201 CREATED
        assertEquals(
            HttpStatus.OK,
            HttpStatus.valueOf(response.getStatusCode().value())
        );
        assertEquals("ACTION COMPLETED", response.getBody().getMessage());
        Vessel test = new Vessel(
            UUID.fromString("a8392946-b34f-41e8-b1bf-82cb9c11aacd"),
            "Tanker",
            "Yellow"
        );
        Vessel actual = (Vessel) response.getBody().getData();
        assertEquals(test.getType(), actual.getType());
        assertEquals(test.getColor(), actual.getColor());
        assertEquals(test.getId(), actual.getId());
    }

    @Test
    void testDelete() {
        UUID id = UUID.fromString("bb39cc00-eff8-4d8a-b2c7-122f31e20a50");

        RequestDTO<UUID> req = new RequestDTO<>(RequestType.DELETE, id);
        ResponseEntity<ResponseDTO> response = getResponseEntity(
            req,
            HttpMethod.DELETE
        );

        // Validate that the response status is HTTP 201 CREATED
        assertEquals(
            HttpStatus.OK,
            HttpStatus.valueOf(response.getStatusCode().value())
        );
        assertEquals("ACTION COMPLETED", response.getBody().getMessage());

        RequestDTO<UUID> checkReq = new RequestDTO<>(RequestType.GETBYID, id);

        ResponseEntity<ResponseDTO> checkResponse = getResponseEntity(
            checkReq,
            HttpMethod.POST
        );

        assertEquals("NO VESSEL FOUND", checkResponse.getBody().getMessage());
    }

    private ResponseEntity<ResponseDTO> getResponseEntity(
        RequestDTO<?> req,
        HttpMethod method
    ) {
        String url = "http://localhost:" + port + "/api"; //URL
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestDTO<?>> httpEntity = new HttpEntity<>(
            req, // The body of the request containing the request type and vessel data
            headers // The headers for the request, specifying the content type
        );
        return restTemplate.exchange(
            url, // The endpoint URL
            method, // The HTTP method (POST to create a resource)
            httpEntity, // The entity to send in the request
            ResponseDTO.class // The response type, which is expected to be a Vessel object
        );
    }
}
