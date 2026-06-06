package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//DTO is not an Entity its just Template to expose to client/User
public class ProductDTO {
    private Long productId;
    @NotBlank(message = "Product Name can't be blank!!!")
    @Size(min=3, message = "Product Name must have atleast 3 characters!!!")
    private String productName;
    private String image;
    private Integer quantity;

    @NotBlank(message = "Product Description can't be blank!!!")
    @Size(min=6, message = "Product Description must have atleast 6 characters!!!")
    private String description;

    private double price;
    private double discount;
    private double specialPrice;
}
