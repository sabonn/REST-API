package com.example.springboot.CustomData;

import java.util.UUID;

/**
 * Enum representing different types of requests.
 * Each enum value contains information about the type of object it works with
 * and the corresponding SQL query used to handle the request.
 */
public enum RequestType {
    // Represents a request to create a new Vessel entry in the database.
    CREATE(
        Vessel.class,
        "INSERT INTO vessel (id, type, color) VALUES (:id, :type, :color)"
    ),

    // Represents a request to fetch a Vessel entry by its unique ID.
    GETBYID(UUID.class, "SELECT * FROM vessel WHERE id = :id"),

    // Represents a request to update an existing Vessel entry.
    UPDATE(
        Vessel.class,
        "UPDATE vessel SET type = :type, color = :color WHERE id = :id"
    ),

    // Represents a request to delete a Vessel entry by its unique ID.
    DELETE(UUID.class, "DELETE FROM vessel WHERE id = :id"),

    // Represents a request to fetch Vessel entries based on their color.
    GETBYCOLOR(String.class, "SELECT * FROM vessel WHERE color = :color");

    // The SQL query associated with the request type.
    private final String sql;

    // The class type of the object that this request operates on.
    private final Class<?> workClass;

    /**
     * Constructor to initialize a RequestType enum value.
     *
     * @param workClass The class type of the data this request works with.
     * @param sql       The SQL query associated with the request type.
     */
    RequestType(Class<?> workClass, String sql) {
        this.sql = sql;
        this.workClass = workClass;
    }

    /**
     * Retrieves the SQL query associated with the request type.
     *
     * @return The SQL query as a string.
     */
    public String getRequestTypeSql() {
        return sql;
    }

    /**
     * Retrieves the class type of the object this request type operates on.
     *
     * @return The class type as a `Class<?>`.
     */
    public Class<?> getWorkClassType() {
        return workClass;
    }
}
