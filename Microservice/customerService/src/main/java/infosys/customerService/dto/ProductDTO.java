package infosys.customerService.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

    private Long productId;

    private String productName;

    private String description;

    private String country;
}
