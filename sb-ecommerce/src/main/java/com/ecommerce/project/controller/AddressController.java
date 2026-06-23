package com.ecommerce.project.controller;

import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping("/addresses")
    public ResponseEntity<?> getAllAddresses(){
        List<AddressDTO> list  = addressService.getAllAddresses();
        return new ResponseEntity<>(list, HttpStatus.FOUND);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<?> getAllAddresses(@PathVariable Long addressId){
        AddressDTO addressDTO  = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.FOUND);
    }

    @GetMapping("/user/addresses")
    public ResponseEntity<?> getAddressOfLoggedInUser(){
        List<AddressDTO>list = addressService.getAddressOfLoggedInUser();
        return new ResponseEntity<>(list, HttpStatus.FOUND);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO){
        AddressDTO savedAddressDTO = addressService.updateAddress(addressId, addressDTO);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
