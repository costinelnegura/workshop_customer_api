package co.uk.negura.workshop_customer_api.controler;

import co.uk.negura.workshop_customer_api.model.CustomerEntity;
import co.uk.negura.workshop_customer_api.service.CustomerService;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*
    Create a new customer and save the customer details.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerEntity customer,
                                            @RequestHeader(value="Authorization") String bearerToken){
        return customerService.createCustomer(customer, bearerToken);
    }

    /*
    Get customer details using the ID.
     */
    @GetMapping(value = "/{ID}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long ID,
                                             @RequestHeader(value="Authorization") String bearerToken){
        return customerService.getCustomerById(ID, bearerToken);
    }

    /*
    Update customer details using the ID and a JSon object containing the new customer info, and then save the updated customer details.
     */
    @PatchMapping("/{ID}")
    public ResponseEntity<?> updateCustomerDetails(@PathVariable Long ID, @RequestBody JsonPatch patch){
        return customerService.updateCustomer(ID, patch);
    }

    /*
    Delete customer details using the ID.
     */
    @DeleteMapping(value = "/{ID}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long ID){
        return customerService.deleteCustomer(ID);
    }
}
