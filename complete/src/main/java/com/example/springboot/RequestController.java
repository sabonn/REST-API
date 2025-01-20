package com.example.springboot;

import com.example.springboot.CustomData.RequestDTO;
import com.example.springboot.CustomData.RequestType;
import com.example.springboot.CustomData.ResponseDTO;
import com.example.springboot.CustomData.Vessel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller class to handle REST-API requests for Vessel DB managment
@RestController
@RequestMapping("/api")
public class RequestController {

    private final VesselRepository vesselRepository;

    // Constructor injection for the vessel repository(handels interaction with the DB)
    public RequestController(VesselRepository vesselRepository) {
        this.vesselRepository = vesselRepository;
    }

    /**
     * Handles POST requests to create a new vessel.
     *
     * @param req The incoming request containing data and request type.
     * @return ResponseEntity with the created vessel or an error message.
     */
    @PostMapping
    public ResponseEntity<ResponseDTO<?>> handelPost(
        @RequestBody RequestDTO<?> req
    ) {
        // Define accepted request types for this handler
        RequestType[] acceptedRequests = {
            RequestType.CREATE,
            RequestType.GETBYID,
            RequestType.GETBYCOLOR,
        };

        // Check if the incoming request is valid
        boolean response = checkCorrectRequest(
            req.getRequestType(),
            acceptedRequests,
            req.getDataType()
        );

        if (response) {
            switch (req.getRequestType()) {
                case CREATE -> {
                    // Ensure the data is properly cast to a Vessel
                    Vessel vessel = (Vessel) req.getData();
                    UUID id = UUID.randomUUID();
                    Map<String, Object> params = Map.of("id", id);
                    ResponseEntity<ResponseDTO<?>> res = queryData(
                        params,
                        RequestType.GETBYID,
                        false
                    );
                    while (res.getBody().getMessage() == "VESSEL FOUND") {
                        id = UUID.randomUUID();
                        params = Map.of("id", id);
                        res = queryData(params, RequestType.GETBYID, false);
                    }
                    return updateData(vessel, RequestType.CREATE, true);
                }
                case GETBYID -> {
                    // Retrieve a vessel by its ID
                    UUID id = (UUID) req.getData();
                    Map<String, Object> params = Map.of("id", id);
                    return queryData(params, RequestType.GETBYID, false);
                }
                case GETBYCOLOR -> {
                    // Retrieve vessels by their color
                    String color = (String) req.getData();
                    Map<String, Object> params = Map.of("color", color);
                    return queryData(params, RequestType.GETBYCOLOR, true);
                }
                default -> {} // No action for unused cases, but ready for future additions
            }
        }

        // Return an error if the request type or data is invalid
        ResponseDTO<String> responseDTO = new ResponseDTO<String>(
            "INCORRECT REQUEST",
            "could be problem with either RequestType or the Data that you send"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles PUT requests to update an existing vessel.
     *
     * @param req The incoming request containing data and request type.
     * @return ResponseEntity with the updated vessel or an error message.
     */
    @PutMapping
    public ResponseEntity<ResponseDTO<?>> handelPut(
        @RequestBody RequestDTO<?> req
    ) {
        // Define accepted request types for this handler
        RequestType[] acceptedRequests = { RequestType.UPDATE };

        // Check if the incoming request is valid
        boolean response = checkCorrectRequest(
            req.getRequestType(),
            acceptedRequests,
            req.getDataType()
        );

        if (response) {
            switch (req.getRequestType()) {
                case UPDATE -> {
                    // Update the vessel data using the repository
                    Vessel vessel = (Vessel) req.getData();
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", vessel.getId());
                    ResponseEntity<ResponseDTO<?>> res = queryData(
                        params,
                        RequestType.GETBYID,
                        false
                    );
                    if (
                        res.getBody().getData().getClass() == Vessel.class
                    ) return updateData(vessel, RequestType.UPDATE, false);
                    else return res;
                }
                default -> {} // No action for unused cases, but ready for future additions
            }
        }

        // Return an error if the request type or data is invalid
        ResponseDTO<String> responseDTO = new ResponseDTO<String>(
            "INCORRECT REQUEST",
            "could be problem with either RequestType or the Data that you send"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DELETE requests to delete a vessel.
     *
     * @param req The incoming request containing data and request type.
     * @return ResponseEntity with the deleted vessel or an error message.
     */
    @DeleteMapping
    public ResponseEntity<ResponseDTO<?>> handelDelete(
        @RequestBody RequestDTO<?> req
    ) {
        // Define accepted request types for this handler
        RequestType[] acceptedRequests = { RequestType.DELETE };
        System.out.println(req);
        // Check if the incoming request is valid
        boolean response = checkCorrectRequest(
            req.getRequestType(),
            acceptedRequests,
            req.getDataType()
        );

        if (response) {
            switch (req.getRequestType()) {
                case DELETE -> {
                    // Delete the vessel by setting its ID
                    UUID id = (UUID) req.getData();
                    Vessel vessel = new Vessel(id, "", "");
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", vessel.getId());
                    ResponseEntity<ResponseDTO<?>> res = queryData(
                        params,
                        RequestType.GETBYID,
                        false
                    );
                    if (
                        res.getBody().getData().getClass() == Vessel.class
                    ) return updateData(vessel, RequestType.DELETE, false);
                    else return res;
                }
                default -> {} // No action for unused cases, but ready for future additions
            }
        }

        // Return an error if the request type or data is invalid
        ResponseDTO<String> responseDTO = new ResponseDTO<String>(
            "INCORRECT REQUEST",
            "could be problem with either RequestType or the Data that you send"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Validates if the request type and data type are acceptable for processing.
     *
     * @param typeOfRequest The type of request being processed.
     * @param expectedTypes The list of accepted request types.
     * @param workClass The expected class type for the data.
     * @return True if the request type and data type match; false otherwise.
     */
    private boolean checkCorrectRequest(
        RequestType typeOfRequest,
        RequestType[] expectedTypes,
        Class<?> workClass
    ) {
        for (RequestType expectedType : expectedTypes) {
            if (
                expectedType == typeOfRequest &&
                typeOfRequest.getWorkClassType() ==
                expectedType.getWorkClassType()
            ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the vessel data in the repository.
     *
     * @param vessel The vessel data to be updated.
     * @param type The request type (CREATE, UPDATE, DELETE).
     * @param create Flag indicating whether the operation is for creating a new record.
     * @return ResponseEntity with the result of the update operation.
     */
    private ResponseEntity<ResponseDTO<?>> updateData(
        Vessel vessel,
        RequestType type,
        boolean create
    ) {
        try {
            vessel = vesselRepository.updateSql(
                vessel,
                type.getRequestTypeSql()
            );
            ResponseDTO<Vessel> responseDTO = new ResponseDTO<Vessel>(
                "ACTION COMPLETED",
                vessel
            );
            return new ResponseEntity<>(
                responseDTO,
                create ? HttpStatus.CREATED : HttpStatus.OK
            );
        } catch (Error e) {
            ResponseDTO<String> responseDTO = new ResponseDTO<String>(
                "FAILED ACTION",
                e.toString()
            );
            return new ResponseEntity<>(
                responseDTO,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Queries the repository for vessels based on the provided parameters.
     *
     * @param params The query parameters to filter vessels.
     * @param type The request type (GETBYID, GETBYCOLOR).
     * @param multipleEntities Flag indicating whether to return a list of vessels.
     * @return ResponseEntity with the found vessels or an error message.
     */
    private ResponseEntity<ResponseDTO<?>> queryData(
        Map<String, Object> params,
        RequestType type,
        boolean multipleEntities
    ) {
        try {
            List<Vessel> vessels = vesselRepository.getVesselByQuery(
                params,
                type.getRequestTypeSql()
            );
            if (vessels == null) {
                ResponseDTO<String> responseDTO = new ResponseDTO<>(
                    "NO VESSEL FOUND",
                    "NONE"
                );
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else if (multipleEntities) {
                ResponseDTO<List<Vessel>> responseDTO = new ResponseDTO<>(
                    "VESSEL'S FOUND",
                    vessels
                );
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
            ResponseDTO<Vessel> responseDTO = new ResponseDTO<>(
                "VESSEL FOUND",
                vessels.get(0)
            );
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Error e) {
            ResponseDTO<String> responseDTO = new ResponseDTO<String>(
                "FAILED QUERY",
                e.toString()
            );
            return new ResponseEntity<>(
                responseDTO,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
