package co.uk.negura.workshop_customer_api.controler;

import co.uk.negura.workshop_customer_api.model.CustomerEntity;
import co.uk.negura.workshop_customer_api.service.CustomerService;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Create a new customer and save the customer details.
     */
    @PostMapping()
    public ResponseEntity<?> createCustomer(@RequestBody CustomerEntity customer){
        return customerService.createCustomer(customer);
    }

    /**
     * Update customer details by using the ID to find it and update it with the new customer details from the JsonPatch.
     */
    @PatchMapping("/{ID}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long ID,
                                            @RequestBody JsonPatch patch){
        return customerService.updateCustomer(ID, patch);
    }

    /**
     * Search for a customer using the ID or the email, can be potentially extended to search for customer based on other parameters.
     */
    @GetMapping()
    public ResponseEntity<?> searchCustomer(@RequestBody Map<String, String> searchRequest){
        return customerService.searchCustomer(searchRequest);
    }

    /**
     * Get all customers
     * @return List of all customers
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    /**
     * Delete customer details using the ID, can be potentially extended to delete customer based on other parameters.
     * @param searchRequest the search request containing the ID or email of the customer to be deleted from the database
     */
    @DeleteMapping()
    public ResponseEntity<?> deleteCustomer(@RequestBody Map<String, Long> searchRequest){
        return customerService.deleteCustomer(searchRequest);
    }
}
