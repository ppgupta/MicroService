package infosys.customerService.Service;

import infosys.customerService.Entity.Customer;
import infosys.customerService.Repository.CustomerRepository;
import infosys.customerService.client.ProductServiceClient;
import infosys.customerService.dto.ProductDTO;
import infosys.customerService.exceptionHandler.CustomerNotFoundException;
import infosys.customerService.exceptionHandler.ProductNotFoundException;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    @Autowired
    private ProductServiceClient productServiceClient;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //save all the customers to the database
    public List<Customer> saveAllCustomer(List<Customer> customer) {
        if (customer == null || customer.isEmpty()) {
            log.warn("Attempted to save an empty or null list of customers");
            return List.of();
        }
        log.info("Saving {} customers", customer.size());
        return customerRepository.saveAll(customer);
    }

    //Retrieve all the customers
    public List<Customer> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll();
    }

    //Find a customer by ID with optional check and exception handling

    public Customer findById(long customerId) {
        log.info("Fetching customer with ID: {}", customerId);
        return customerRepository.findById(customerId).orElseThrow(
                () -> {
                    log.error("Customer with ID {} not found for update. ", customerId);
                    return new CustomerNotFoundException("Customer not found");
                }
        );
    }

    //Update a customer partially
    public Customer updatePartially(long customerId, Customer customer) {
        if (customer == null) {
            log.warn("Attempted to update with null customer data for ID: {}", customerId);
            return null;
        }

        log.info("Updating customer with ID:{}", customerId);
        final Customer existingCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> {
                    log.error("Customer with ID {} not found for update. ", customerId);
                    return new CustomerNotFoundException("Customer not found");
                }
        );
        if (existingCustomer.getName() != null) {
            existingCustomer.setName(customer.getName());
        }
        return customerRepository.save(existingCustomer);
    }

    public void deleteById(long customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        customerRepository.deleteById(customerId);
        log.info("Customer with ID {} deleted sucessfully", customerId);
    }

    public List<ProductDTO> getProductDetailsForCustomer(String customerCountry) {

        // Fetch product details from ProductService
        List<ProductDTO> products = productServiceClient.getProductDetails();
        if (products == null || products.isEmpty()) {
            throw new ProductNotFoundException("Product not found for country : " + customerCountry );
        }
        return products.stream()
                .filter(product->product.getCountry().equalsIgnoreCase(customerCountry))
                .collect(Collectors.toList());
    }


}
