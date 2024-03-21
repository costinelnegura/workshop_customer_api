package co.uk.negura.workshop_customer_api.service;

import co.uk.negura.workshop_customer_api.model.CustomerEntity;
import co.uk.negura.workshop_customer_api.repository.CustomerRepository;
import co.uk.negura.workshop_customer_api.util.HandleResponseUtil;
import co.uk.negura.workshop_customer_api.util.ValidateTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {


    private final CustomerRepository customerRepository;

    private final ValidateTokenUtil validateTokenUtil;

    private final HandleResponseUtil handleResponseUtil;

    public CustomerService(CustomerRepository customerRepository,
                           ValidateTokenUtil validateTokenUtil,
                           HandleResponseUtil handleResponseUtil) {
        this.customerRepository = customerRepository;
        this.validateTokenUtil = validateTokenUtil;
        this.handleResponseUtil = handleResponseUtil;
    }

    /*
        Validate bearer token.
         */
    private ResponseEntity<?> validateToken(String bearerToken) {
        return validateTokenUtil.validateToken(bearerToken);
    }

    /*
    Create a new customer and save the customer details.
     */
    public ResponseEntity<?> createCustomer(CustomerEntity customer, String bearerToken){
        ResponseEntity<?> tokenValidationResponse = validateToken(bearerToken);
        if (tokenValidationResponse.getStatusCode().value() != 200) {
            return tokenValidationResponse;
        }
        CustomerEntity newCustomer = customerRepository.save(customer);
        return handleResponseUtil.handleResponse(Optional.of(newCustomer),
                tokenValidationResponse, "Customer created successfully",
                HandleResponseUtil.OperationType.CREATE);
    }

    /*
    Search for a customer using the ID or the email, can be potentially extended to search for customer based on other parameters.
     */
    public ResponseEntity<?> searchCustomer(Map<String, String> searchRequest, String bearerToken){
        ResponseEntity<?> tokenValidationResponse = validateToken(bearerToken);
        if(tokenValidationResponse.getStatusCode().value() != 200){
            return tokenValidationResponse;
        } else if (searchRequest.containsKey("email")) {
            String email = searchRequest.get("email");
            Optional<CustomerEntity> customer = customerRepository.findByEmail(email);
            if (customer.isEmpty()) {
                Map<String, Object> body = new HashMap<>();
                body.put("status", 404);
                body.put("message", "Customer not found");
                return ResponseEntity.status(404).header(
                        "Message", "Customer not found"
                ).body(body);
            }
            return handleResponseUtil.handleResponse(customer,
                    tokenValidationResponse,
                    "Customer retrieved successfully",
                    HandleResponseUtil.OperationType.SEARCH);
        } else if (searchRequest.containsKey("id")) {
            try {
                Long id = Long.parseLong(searchRequest.get("id"));
                Optional<CustomerEntity> customer = customerRepository.findById(id);
                if (customer.isEmpty()) {
                    Map<String, Object> body = new HashMap<>();
                    body.put("status", 404);
                    body.put("message", "Customer not found");
                    return ResponseEntity.status(404).header(
                            "Message", "Customer not found"
                    ).body(body);
                }
                return handleResponseUtil.handleResponse(customer,
                        tokenValidationResponse,
                        "Customer retrieved successfully",
                        HandleResponseUtil.OperationType.SEARCH);
            } catch (NumberFormatException e) {
                Map<String, Object> body = new HashMap<>();
                body.put("status", 403);
                body.put("message", "Invalid Input");
                return ResponseEntity.status(403).header(
                        "Message", "Invalid input"
                ).body(body);
            }
        } else {
            Map<String, Object> body = new HashMap<>();
            body.put("status", 404);
            body.put("message", "Bad Request");
            return ResponseEntity.status(400).header(
                    "Message", "Bad Request"
            ).body(body);
        }
    }

    /*
    Update customer details by using the ID to find it and update it with the new customer details from the JsonPatch.
     */
    public ResponseEntity<?> updateCustomer(Long ID, JsonPatch patch, String bearerToken){
        ResponseEntity<?> tokenValidationResponse = validateToken(bearerToken);
           if(tokenValidationResponse.getStatusCode().value() != 200){
                return tokenValidationResponse;
            }
        try {
            CustomerEntity user = customerRepository.findById(ID).orElseThrow(ChangeSetPersister.NotFoundException::new);
            CustomerEntity patchedUser = (applyPatchToCustomer(patch, user));
            customerRepository.save(patchedUser);
            return handleResponseUtil.handleResponse(Optional.of(patchedUser),
                    tokenValidationResponse,
                    "Customer updated successfully",
                    HandleResponseUtil.OperationType.UPDATE);
        } catch (ChangeSetPersister.NotFoundException e) {
            // Handle the case when the user is not found.
            return ResponseEntity.notFound().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            // Handle JSON patch or processing exceptions.
            return ResponseEntity.internalServerError().body("Internal Server Error: JSON Patch Error");
        } catch (Exception e) {
            // Handle any other unexpected exceptions.
            return ResponseEntity.internalServerError().body("Internal Server Error: Something went wrong");
        }
    }

    /*
    Apply the patch to the customer object.
     */
    private CustomerEntity applyPatchToCustomer(JsonPatch patch, CustomerEntity targetCustomer)
            throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, CustomerEntity.class);
    }

    /*
    Delete customer details using the ID, can be potentially extended to delete customer based on other parameters.
     */
    public ResponseEntity<?> deleteCustomer(Map<String, Long> searchRequest, String bearerToken){
        ResponseEntity<?> tokenValidationResponse = validateToken(bearerToken);
        if(tokenValidationResponse.getStatusCode().value() != 200){
            return tokenValidationResponse;
        } else if (searchRequest.containsKey("id")) {
            Long id = searchRequest.get("id");
            if(customerRepository.existsById(id)){
                customerRepository.deleteById(id);
                return ResponseEntity.status(200).header(
                        "Message", "Customer deleted successfully"
                ).build();
            } else {
                return ResponseEntity.status(404).header(
                        "Message", "Customer not found"
                ).build();
            }
        } else {
            return ResponseEntity.status(400).header(
                    "Message", "Bad Request"
            ).build();
        }
    }
}