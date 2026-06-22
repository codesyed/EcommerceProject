package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor//??can we remove this
@NoArgsConstructor
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //?
    private Long productId;
    @NotBlank(message = "Product Name can't be blank!!!")
    @Size(min=3, message = "Product Name must have atleast 3 characters!!!")
    private String productName;
    @NotBlank(message = "Product Description can't be blank!!!")
    @Size(min=6, message = "Product Description must have atleast 6 characters!!!")
    private String description;
    private String image;
    private Integer quantity;
    private Double discount;
    private Double price;
    private Double specialPrice;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private User user;


    @OneToMany(mappedBy = "product",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER)
            /*
             CascadeType.REMOVE is intentionally not used because deleting a Product requires additional business logic,
             such as updating Cart totalPrice.

             Therefor Product deletion is handled explicitly in ProductService instead of relying on automatic cascade deletion.

             If Product.getCartItemList().remove(cartItem) should automatically delete the CartItem from DB,
             then orphanRemoval=true can be considered BUT We need to execute business logic with that thereby avoiding this.

             orphanRemoval=true means if Product.cartItemsList.remove(xItem) then delete xItem from DB
             */

    private List<CartItem> cartItemList;
}
