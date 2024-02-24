package co.uk.negura.workshop_customer_api.service;

import co.uk.negura.workshop_customer_api.model.CustomerEntity;
import co.uk.negura.workshop_customer_api.repository.CustomerRepository;
import co.uk.negura.workshop_customer_api.util.ValidateTokenUtil;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    ValidateTokenUtil validateTokenUtil;

    public ResponseEntity<?> createCustomer(CustomerEntity customer, String bearerToken){
        ResponseEntity<?> tokenValidationResponse = validateTokenUtil.validateToken(bearerToken);
        if (tokenValidationResponse.getStatusCode().value() == 200) {
            customerRepository.save(customer);
            return ResponseEntity.status(201).header(
                    "Message", "Customer created successfully"
            ).build();
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

    public ResponseEntity<?> getCustomerById(Long ID, String bearerToken){
        ResponseEntity<?> tokenValidationResponse = validateTokenUtil.validateToken(bearerToken);
        if (tokenValidationResponse.getStatusCode().value() == 200) {
            Optional<CustomerEntity> customer = customerRepository.findById(ID);
            return customer.isPresent() ?
                    ResponseEntity.status(200).header(
                            "Message", "Customer details retrieved successfully"
                    ).body(customer.get()) :
                    ResponseEntity.status(404).header(
                            "Message", "Customer not found"
                    ).build();
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

    public ResponseEntity<?> updateCustomer(Long ID, JsonPatch patch){
        System.out.println("updateCustomer method called");
        return null;
    }

    public ResponseEntity<?> deleteCustomer(Long ID){
        System.out.println("deleteCustomer method called");
        return null;
    }
}
