package infosys.customerService.client;

import infosys.customerService.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductServiceClient {

    private final RestTemplate restTemplate;
    private static final String PRODUCT_SERVICE_BASE_URL = "http://localhost:8080/api/products/get-all-products";

    public ProductServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProductDTO> getProductDetails() {
        // Construct the URL for the ProductService API
        String url = PRODUCT_SERVICE_BASE_URL ;

        ResponseEntity<List<ProductDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductDTO>>() {
        });
        if (response.getStatusCode().is2xxSuccessful()) {
            List<ProductDTO> products = response.getBody();
            return products;
        } else {
            throw new RuntimeException("Failed to fetch product details. HTTP Status: " + response.getStatusCode());
        }
    }
}
