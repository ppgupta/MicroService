package infosys.customerService.Controllers;

import infosys.customerService.Entity.Customer;
import infosys.customerService.Service.CustomerService;
import infosys.customerService.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("customerServiceAPI")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Save All Customers", description = "This API is to save one or more customers to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Customers Added Successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong in the service", content = @Content)})
    @PostMapping("/saveCustomer")
    public ResponseEntity<List<Customer>> saveCustomerToDatabase(@RequestBody List<Customer> customer) {
        final List<Customer> savedCustomer= customerService.saveAllCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve All Customers", description = "Fetches a list of all customer from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers fetched successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong in the service")})
    @GetMapping("/findAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }


    @Operation(summary = "Fetch a customer by ID", description = "Fetches a customer from the database by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer Found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @GetMapping("/findCustomerById/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long customerId) {
        return new ResponseEntity<>(customerService.findById(customerId), HttpStatus.OK);
    }


    @Operation(summary = "Update customer partially", description = "Update some details of a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer Updated Successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)})
    @PatchMapping("/partialUpdateCustomer/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable long customerId, @RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.updatePartially(customerId, customer), HttpStatus.OK);
    }

    @Operation(summary = "Delete a customer by ID", description = "Delete a customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @DeleteMapping("/deleteCustomer/{customerId}")
    public String deleteCustomer(@PathVariable long customerId) {
        customerService.deleteById(customerId);
        return "Record deleted Successfully";
    }


    @Operation(summary = "Retrieve All Products based on Product id", description = "Fetches a list of all products from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Products fetched successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong in the service")})

    @GetMapping("/customer/products")
    public ResponseEntity<List<ProductDTO>> getOrderWithProductDetails(@RequestParam String country) {
        String methodName = "getOrderWithProductDetails()";
        log.info("[{}] started ", methodName);

        List<ProductDTO> product = customerService.getProductDetailsForCustomer(country );
        return ResponseEntity.ok(product);
    }

}