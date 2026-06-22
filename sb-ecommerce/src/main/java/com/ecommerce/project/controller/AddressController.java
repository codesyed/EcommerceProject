package com.ecommerce.project.controller;

import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<?> addAddressForLoggedInUser(@RequestBody AddressDTO addressDTO){

        AddressDTO savedAddressDTO = addressService.addAddressForLoggedInUser(addressDTO);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }
}
