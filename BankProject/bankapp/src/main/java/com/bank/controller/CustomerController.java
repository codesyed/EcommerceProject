package com.bank.controller;

import com.bank.customer.Customer;
import com.bank.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/customers") //check-value
public class CustomerController {

    private final ServiceImpl service;

    public CustomerController(ServiceImpl service){
        this.service = service;
        System.out.println("****Default constructor of CustomerController****");
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody  Customer customer){
        Customer cus =  service.createCustomer(customer);
        return ResponseEntity.ok(cus);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer>list = service.getAllCustomers();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(service.getCustomerById(id));
        } catch (ResponseStatusException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{oldId}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer newCustomer, @PathVariable Long oldId){
        try {
            return ResponseEntity.ok(service.updateCustomer(newCustomer, oldId));
        }
        catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String>  deleteCustomer(@PathVariable Long id){
        try {
            service.deleteCustomer(id);
            return ResponseEntity.ok("Deletion of id: "+ id+" Success!");
        }
        catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getReason());
        }
    }
}
