package co.uk.negura.workshop_customer_api.service;

import co.uk.negura.workshop_customer_api.model.CustomerEntity;
import co.uk.negura.workshop_customer_api.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CustomerService {


    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    /**
     * Create a new customer and save the customer details.
     * @param customer the customer details to be saved
     * @return the response entity containing the customer details
     */
    public ResponseEntity<?> createCustomer(CustomerEntity customer){
        CustomerEntity newCustomer = customerRepository.save(customer);
        return ResponseEntity.status(201).header(
                "Message", "Customer created successfully"
        ).body(newCustomer);
    }

    /**
     * Search for a customer using the ID or the email, can be potentially extended to search for customer based on other parameters.
     * @param searchRequest the search request containing the ID or email of the customer to be searched from the database.
     */
    public ResponseEntity<?> searchCustomer(Map<String, String> searchRequest){
        Map<String, Object> map = new LinkedHashMap<>();
        if (searchRequest.containsKey("email")) {
            String email = searchRequest.get("email");
            CustomerEntity customer = customerRepository.findByEmail(email).orElse(null);
            if(customer == null){
                map.put("object", "error");
                map.put("status", 400);
                map.put("message", "Customer not found");
                return ResponseEntity.badRequest().body(map);
            }
            return ResponseEntity.ok().body(customer);
        } else if (searchRequest.containsKey("id")) {
            Long id = Long.parseLong(searchRequest.get("id"));
            CustomerEntity customer = customerRepository.findById(id).orElse(null);
            if(customer == null){
                map.put("object", "error");
                map.put("status", 400);
                map.put("message", "Customer not found");
                return ResponseEntity.badRequest().body(map);
            }
            return ResponseEntity.ok().body(customer);
        } else {
            map.put("object", "error");
            map.put("status", 400);
            map.put("message", "Bad Request");
            return ResponseEntity.badRequest().body(map);
        }
    }

    /**
     * Update customer details by using the ID to find it and update it with the new customer details from the JsonPatch.
     * @param ID the ID of the customer to be updated
     * @param patch the JsonPatch containing the new customer details to be updated
     */
    public ResponseEntity<?> updateCustomer(Long ID, JsonPatch patch){
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            CustomerEntity user = customerRepository.findById(ID).orElseThrow(ChangeSetPersister.NotFoundException::new);
            CustomerEntity patchedUser = (applyPatchToCustomer(patch, user));
            customerRepository.save(patchedUser);
            return ResponseEntity.ok().body("Customer updated successfully");
        } catch (ChangeSetPersister.NotFoundException e) {
            map.put("object", "error");
            map.put("status", 404);
            map.put("message", "Customer not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } catch (JsonPatchException | JsonProcessingException e) {
            map.put("object", "error");
            map.put("status", 500);
            map.put("message", "Error updating customer. Please check the JSON Patch");
            return ResponseEntity.internalServerError().body(map);
        } catch (Exception e) {
            map.put("object", "error");
            map.put("status", 500);
            map.put("message", "Internal Server Error: Something went wrong");
            return ResponseEntity.internalServerError().body(map);
        }
    }

    /**
     * Apply the patch to the customer object.
     * @param patch the JsonPatch containing the new customer details to be updated
     * @param targetCustomer the customer object to be updated
     */
    private CustomerEntity applyPatchToCustomer(JsonPatch patch, CustomerEntity targetCustomer)
            throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, CustomerEntity.class);
    }

    /**
     * Delete customer details using the ID, can be potentially extended to delete customer based on other parameters.
     * @param searchRequest the search request containing the ID or email of the customer to be deleted from the database
     */
    public ResponseEntity<?> deleteCustomer(Map<String, Long> searchRequest){
        Map<String, Object> map = new LinkedHashMap<>();
         if (searchRequest.containsKey("id")) {
            Long id = searchRequest.get("id");
            if(customerRepository.existsById(id)){
                customerRepository.deleteById(id);
                return ResponseEntity.status(200).header(
                        "Message", "Customer deleted successfully"
                ).build();
            } else {
                map.put("object", "error");
                map.put("status", 404);
                map.put("message", "Customer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            }
        } else {
            map.put("object", "error");
            map.put("status", 400);
            map.put("message", "Bad Request");
            return ResponseEntity.badRequest().body(map);
        }
    }

    /**
     * Get all customers from the database.
     * @return List of all customers
     */
    public ResponseEntity<?> getAllCustomers() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (customerRepository.findAll().isEmpty()) {
            map.put("object", "error");
            map.put("status", 404);
            map.put("message", "No customers found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {
            return ResponseEntity.ok().body(customerRepository.findAll());
        }
    }
}