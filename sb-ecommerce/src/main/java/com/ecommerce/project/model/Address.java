package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "addresses")
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank(message = "street cannot be blank")
    @Size(min=3, message = "Street name must be at-least 5 characters")
    private String street;

    @NotBlank(message = "Building name cannot be blank")
    @Size(min=3, message = "Building name must be at-least 3 characters")
    private String buildingName;

    @NotBlank(message = "city cannot be blank")
    @Size(min=3, message = "city must be at-least 3 characters")
    private String city;

    @NotBlank(message = "state name cannot be blank")
    @Size(min=3, message = "state name must be at-least 3 characters")
    private String state;

    @NotBlank(message = "pin-code cannot be blank")
    @Size(min=5, message = "pin-code must be at-least 5 characters")
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String buildingName, String city, String state, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }
}
