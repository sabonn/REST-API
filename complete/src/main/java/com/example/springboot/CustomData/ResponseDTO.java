package com.example.springboot.CustomData;

import com.example.springboot.Deserializers.ResponseDTODeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A generic data transfer object (DTO) used to handle responses.
 * It encapsulates the status, message, and associated data.
 *
 * @param <T> The type of the data associated with the response.
 */
@JsonDeserialize(using = ResponseDTODeserializer.class)
public class ResponseDTO<T> {

    // A message associated with the response (e.g., a success or error message)
    @JsonProperty("message")
    private String message;

    // The data associated with the response, which can be of any type
    @JsonProperty("data")
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
    )
    private T data;

    /**
     * Constructor to initialize the response with a status, message, and associated data.
     *
     * @param status The status of the response (e.g., "success", "error").
     * @param message The message associated with the response.
     * @param data The data associated with the response.
     */
    public ResponseDTO(String message, T data) {
        this.message = message;
        this.data = data;
    }

    /**
     * Retrieves the message associated with the response.
     *
     * @return The message of the response.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message associated with the response.
     *
     * @param message The new message to set for the response.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retrieves the data associated with the response.
     *
     * @return The data of the response.
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the data associated with the response.
     *
     * @param data The new data to set for the response.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Determines the class type of the data object.
     * This can be used for runtime checks and validation of the data type.
     *
     * @return The class type of the data.
     */
    public Class<?> getDataType() {
        return data != null ? data.getClass() : null;
    }
}
