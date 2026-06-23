package com.ecommerce.project.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
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

    @Override
    public String toString() {
        return "AddressDTO{" +
                ", street='" + street + '\'' +
                ", buildingName='" + buildingName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pincode='" + pincode + '\'' +
                '}';
    }
}
