package com.ecommerce.project.service;

import com.ecommerce.project.payload.AddressDTO;
import org.springframework.stereotype.Service;


public interface AddressService {
    AddressDTO addAddressForLoggedInUser(AddressDTO addressDTO);
}
