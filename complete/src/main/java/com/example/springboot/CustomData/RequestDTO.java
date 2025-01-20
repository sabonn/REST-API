package com.example.springboot.CustomData;

import com.example.springboot.Deserializers.RequestDTODeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A generic data transfer object (DTO) used to handle requests.
 * It encapsulates the request type and the associated data.
 *
 * @param <T> The type of the data associated with the request.
 */
@JsonDeserialize(using = RequestDTODeserializer.class) // Use custom deserializer
public class RequestDTO<T> {

    // The type of the request, which determines the operation to perform.
    @JsonProperty("type")
    private RequestType type;

    // The data associated with the request, which can be of any type.
    @JsonProperty("data")
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
    )
    private T data;

    /**
     * Constructor to initialize the request with a type and its associated data.
     *
     * @param type The type of the request (e.g., CREATE, GETBYID, etc.).
     * @param data The data associated with the request.
     */
    public RequestDTO(RequestType type, T data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Retrieves the data associated with the request.
     *
     * @return The data of the request.
     */
    public T getData() {
        return data;
    }

    /**
     * Retrieves the type of the request.
     *
     * @return The type of the request.
     */
    public RequestType getRequestType() {
        return type;
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
