package com.bank.service;

import com.bank.customer.Customer;

import java.util.List;

public interface Service {
    Customer createCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    Customer updateCustomer(Customer newCustomer, Long oldId);
    void deleteCustomer(Long id);
}
