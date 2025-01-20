package com.example.springboot.Deserializers;

import com.example.springboot.CustomData.RequestDTO;
import com.example.springboot.CustomData.RequestType;
import com.example.springboot.CustomData.Vessel;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.UUID;

public class RequestDTODeserializer extends JsonDeserializer<RequestDTO<?>> {

    @Override
    public RequestDTO<?> deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Deserialize the 'requestType' field
        RequestType type = RequestType.valueOf(
            node.get("requestType").asText()
        );

        // Deserialize the 'data' field based on the type
        JsonNode dataNode = node.get("data");
        Object data = null;

        switch (type) {
            case CREATE, UPDATE -> {
                // Deserialize to a Vessel
                data = p.getCodec().treeToValue(dataNode, Vessel.class);
            }
            case GETBYID, DELETE -> {
                // Use the custom UUIDDeserializer to handle UUID parsing
                String uuidStr = dataNode.get(1).asText();
                data = UUID.fromString(uuidStr);
            }
            case GETBYCOLOR -> {
                // For color, we assume it's just a string
                data = dataNode.asText();
            }
        }

        // Return the constructed RequestDTO object with the corresponding type and data
        return new RequestDTO<>(type, data);
    }
}
