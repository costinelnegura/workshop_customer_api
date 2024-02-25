package co.uk.negura.workshop_customer_api.util;

import co.uk.negura.workshop_customer_api.model.CustomerEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HandleResponseUtil {

    public enum OperationType {
        CREATE, UPDATE, SEARCH, DELETE
    }

    public ResponseEntity<?> handleResponse(Optional<CustomerEntity> customer,
                                            ResponseEntity<?> tokenValidationResponse,
                                            String message,
                                            OperationType operationType) {
        if (tokenValidationResponse.getStatusCode().value() == 200) {
            switch (operationType) {
                case CREATE:
                    return customer.isPresent() ?
                            ResponseEntity.status(201).header(
                                    "Message", message
                            ).body(customer.get()) :
                            ResponseEntity.status(400).header(
                                    "Message", "Customer creation failed"
                            ).build();
                case UPDATE:
                case SEARCH:
                case DELETE:
                    return customer.isPresent() ?
                            ResponseEntity.status(200).header(
                                    "Message", message
                            ).body(customer.get()) :
                            ResponseEntity.status(404).header(
                                    "Message", "Customer not found"
                            ).build();
                default:
                    return ResponseEntity.status(400).header(
                            "Message", "Invalid operation type"
                    ).build();
            }
        } else {
            // Return the failed token validation response
            return tokenValidationResponse.getStatusCode().is4xxClientError() ?
                    ResponseEntity.status(401).header(
                            "Message", "Unauthorized"
                    ).build() :
                    ResponseEntity.status(500).header(
                            "Message", "Internal Server Error"
                    ).build();
        }
    }
}
