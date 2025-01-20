package com.example.springboot.Deserializers;

import com.example.springboot.CustomData.ResponseDTO;
import com.example.springboot.CustomData.Vessel;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.aspectj.weaver.ast.Var;

public class ResponseDTODeserializer extends JsonDeserializer<ResponseDTO<?>> {

    @Override
    public ResponseDTO<?> deserialize(
        JsonParser p,
        DeserializationContext ctxt
    ) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        String message = node.get("message").asText();

        JsonNode dataNode = node.get("data");
        Object data = null;

        switch (message) {
            case "VESSEL FOUND", "ACTION COMPLETED" -> {
                data = p.getCodec().treeToValue(dataNode, Vessel.class);
            }
            case "VESSEL'S FOUND" -> {
                List<Vessel> vesselList = new ArrayList<>();
                JsonNode vesselListNode = dataNode.get(1);
                for (JsonNode vesselNode : vesselListNode) {
                    Vessel vessel = new Vessel(
                        UUID.fromString(vesselNode.get("id").asText()),
                        vesselNode.get("type").asText(),
                        vesselNode.get("color").asText()
                    );
                    vesselList.add(vessel);
                }
                data = vesselList;
            }
            case "NO VESSEL FOUND", "FAILD ACTION" -> {
                data = dataNode.asText();
            }
        }
        ResponseDTO<?> responseDTO = new ResponseDTO<>(message, data);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonRequest = mapper.writeValueAsString(responseDTO);
            System.out.println("Serialized request: " + jsonRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return responseDTO;
    }
}
