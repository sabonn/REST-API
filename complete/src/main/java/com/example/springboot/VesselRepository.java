package com.example.springboot;

import com.example.springboot.CustomData.Vessel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository class for handling database operations related to the Vessel entity.
 */
@Repository
public class VesselRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Constructor to initialize the repository with a JdbcTemplate for database interaction.
     *
     * @param jdbcTemplate The JdbcTemplate instance for executing SQL queries.
     */
    public VesselRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves a list of Vessel objects from the database based on the provided SQL query and arguments.
     *
     * @param args The arguments to be passed into the SQL query (e.g., parameters for WHERE clause).
     * @param sql  The SQL query string to execute.
     * @return A list of Vessel objects matching the query, or null if no results are found.
     */
    public List<Vessel> getVesselByQuery(
        Map<String, Object> params,
        String sql
    ) {
        // Use RowMapper to map ResultSet to Vessel object.
        RowMapper<Vessel> rowMapper = new RowMapper<Vessel>() {
            @Override
            public Vessel mapRow(ResultSet rs, int rowNum) throws SQLException {
                // Extract values from the ResultSet
                UUID vesselId = UUID.fromString(rs.getString("id"));
                String type = rs.getString("type");
                String color = rs.getString("color");

                // Return a new Vessel object based on the ResultSet values
                return new Vessel(vesselId, type, color);
            }
        };

        // Execute the query and map each row to a Vessel object
        List<Vessel> vessels = jdbcTemplate.query(sql, params, rowMapper);

        // Return the list of vessels or null if the list is empty
        return vessels.isEmpty() ? null : vessels;
    }

    /**
     * Executes an SQL update operation for the Vessel entity.
     *
     * @param vessel The Vessel object containing the data to be updated in the database.
     * @param sql    The SQL update query string to execute.
     * @return The updated Vessel object.
     */
    public Vessel updateSql(Vessel vessel, String sql) {
        // Create a MapSqlParameterSource to map named parameters in the SQL query.
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", vessel.getId().toString(), Types.OTHER) // Map "id" parameter to the vessel's ID.
            .addValue("type", vessel.getType(), Types.VARCHAR) // Map "type" parameter to the vessel's type.
            .addValue("color", vessel.getColor(), Types.VARCHAR); // Map "color" parameter to the vessel's color.

        // Execute the SQL update query with the mapped parameters.
        jdbcTemplate.update(sql, params);

        // Return the updated Vessel object.
        return vessel;
    }
}
