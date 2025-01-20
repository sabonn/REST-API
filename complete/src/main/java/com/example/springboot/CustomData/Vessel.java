package com.example.springboot.CustomData;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Vessel class represents a vessel entity in the system with unique identification, type, and color.
 * This class is used to model the data that will be transferred between the client and the server.
 */
@Entity
@Table(name = "vessel")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class"
) // Include type information for proper deserialization
public class Vessel {

    @Id
    @JsonProperty("id") // Serialize and deserialize the id field as "id"
    private UUID id;

    @JsonProperty("type") // Serialize and deserialize the type field as "type"
    private String type;

    @JsonProperty("color") // Serialize and deserialize the color field as "color"
    private String color;

    /**
     * Constructor for creating a Vessel object from a JSON payload.
     * This constructor is annotated with @JsonCreator to indicate how the JSON data is mapped to the object.
     *
     * @param id    The unique identifier of the vessel (UUID). This field uniquely distinguishes one vessel from another.
     * @param type  The type of the vessel (e.g., "cargo", "tanker", "passenger"). This field classifies the vessel based on its function or category.
     * @param color The color of the vessel (e.g., "red", "blue", "green"). This field helps identify the vessel visually or can be used for categorization.
     */
    @JsonCreator // This annotation tells Jackson to use this constructor for deserialization
    public Vessel(
        @JsonProperty("id") UUID id, // Deserialize "id" from JSON to UUID
        @JsonProperty("type") String type, // Deserialize "type" from JSON to String
        @JsonProperty("color") String color // Deserialize "color" from JSON to String
    ) {
        this.id = id;
        this.type = type;
        this.color = color;
    }

    public Vessel(
        @JsonProperty("type") String type, // Deserialize "type" from JSON to String
        @JsonProperty("color") String color // Deserialize "color" from JSON to String
    ) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.color = color;
    }

    /**
     * Getter for the vessel's unique identifier (id).
     *
     * @return The UUID of the vessel.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Setter for the vessel's unique identifier (id).
     *
     * @param id The new UUID to set for the vessel.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Getter for the type of the vessel.
     *
     * @return The type of the vessel (e.g., "cargo", "tanker").
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for the type of the vessel.
     *
     * @param type The new type to set for the vessel (e.g., "cargo", "tanker").
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for the color of the vessel.
     *
     * @return The color of the vessel (e.g., "red", "blue").
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for the color of the vessel.
     *
     * @param color The new color to set for the vessel (e.g., "red", "blue").
     */
    public void setColor(String color) {
        this.color = color;
    }
}
