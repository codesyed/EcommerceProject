package com.ecommerce.project.service;

import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO addAddressForLoggedInUser(AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();

        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        user.getAddressList().add(address);

        User savedUser = userRepository.save(user); //User has cascade.persist for address
        return addressDTO;
    }

    /* -> Can Add pagination and sorting here for practice */
    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addressList = addressRepository.findAll();
        List<AddressDTO> addressDTOS = addressList.stream()
                .map(Ad -> modelMapper.map(Ad, AddressDTO.class))
                .collect(Collectors.toList());
        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddressOfLoggedInUser() {
        User user = authUtil.loggedInUser();
        List<Address> list = user.getAddressList();
        List<AddressDTO>listDTO = list.stream()
                .map(add->modelMapper.map(add, AddressDTO.class))
                .collect(Collectors.toList());
        return listDTO;
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address savedAdd = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));

        savedAdd.setCity(addressDTO.getCity());
        savedAdd.setPincode(addressDTO.getPincode());
        savedAdd.setStreet(addressDTO.getStreet());
        savedAdd.setState(addressDTO.getState());
        savedAdd.setBuildingName(addressDTO.getBuildingName());

        Address updatedAddress = addressRepository.save(savedAdd);

        User user = savedAdd.getUser();
        user.getAddressList().removeIf(add->add.getAddressId().equals(updatedAddress.getAddressId()));
        user.getAddressList().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address savedAdd = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = savedAdd.getUser();
        user.getAddressList().remove(savedAdd); //Inside User we have user orphanRemoval = true;
        userRepository.save(user);
        return "Deletion Success of addressId: "+addressId;
    }
}
