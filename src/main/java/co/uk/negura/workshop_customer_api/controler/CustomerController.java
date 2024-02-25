package co.uk.negura.workshop_customer_api.controler;

import co.uk.negura.workshop_customer_api.model.CustomerEntity;
import co.uk.negura.workshop_customer_api.service.CustomerService;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*
    Create a new customer and save the customer details.
     */
    @PostMapping()
    public ResponseEntity<?> createCustomer(@RequestBody CustomerEntity customer,
                                            @RequestHeader(value="Authorization") String bearerToken){
        return customerService.createCustomer(customer, bearerToken);
    }

    /*
    Update customer details by using the ID to find it and update it with the new customer details from the JsonPatch.
     */
    @PatchMapping("/{ID}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long ID,
                                            @RequestBody JsonPatch patch,
                                            @RequestHeader(value="Authorization") String bearerToken){
        return customerService.updateCustomer(ID, patch, bearerToken);
    }

    /*
    Search for a customer using the ID or the email, can be potentially extended to search for customer based on other parameters.
     */
    @GetMapping()
    public ResponseEntity<?> searchCustomer(@RequestBody Map<String, String> searchRequest,
                                            @RequestHeader(value="Authorization") String bearerToken){
        return customerService.searchCustomer(searchRequest, bearerToken);
    }

    /*
    Delete customer details using the ID, can be potentially extended to delete customer based on other parameters.
     */
    @DeleteMapping()
    public ResponseEntity<?> deleteCustomer(@RequestBody Map<String, Long> searchRequest,
                                            @RequestHeader(value="Authorization") String bearerToken){
        return customerService.deleteCustomer(searchRequest, bearerToken);
    }
}
