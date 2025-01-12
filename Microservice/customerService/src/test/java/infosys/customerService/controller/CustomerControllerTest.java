//package infosys.customerService.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import infosys.customerService.Controllers.CustomerController;
//import infosys.customerService.Entity.Customer;
//import infosys.customerService.Service.CustomerService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(CustomerController.class)
//public class CustomerControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private CustomerService customerService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void testSaveAllCustomer() throws Exception {
//        Mockito.when(customerService.saveAllCustomer(constructCustomer())).
//                thenReturn(constructCustomer());
//        mockMvc.perform(post("/saveCustomer")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(constructCustomer())))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.customerId",is(2)));
//    }
//
//    private List<Customer> constructCustomer() {
//        final List<Customer> customerList = new ArrayList<>();
//        final Customer customer = new Customer();
//        customer.setCustomerId(2L);
//        customer.setName("infosys");
//        customer.setCity("pune");
//        customer.setCountry("India");
//        customer.setAddress("dfghj");
//        customerList.add(customer);
//        return customerList;
//    }
//}
