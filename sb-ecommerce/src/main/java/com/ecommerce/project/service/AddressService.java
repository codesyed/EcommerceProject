package com.ecommerce.project.service;

import com.ecommerce.project.payload.AddressDTO;

import java.util.List;


public interface AddressService {
    AddressDTO addAddressForLoggedInUser(AddressDTO addressDTO);
    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);
    List<AddressDTO> getAddressOfLoggedInUser();

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
