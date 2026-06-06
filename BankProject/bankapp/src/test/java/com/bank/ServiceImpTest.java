package com.bank;

import com.bank.customer.Customer;
import com.bank.repository.CustomerRepository;
import com.bank.service.Service;
import com.bank.service.ServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


/* Unit Testing using Mockito Mocks */
@ExtendWith(MockitoExtension.class)
public class ServiceImpTest {

    @InjectMocks //Creates object of ServiceImpl and injects compatible @Mock objects inside it.
                 //Since Spring is not handling these object and no SpringContext so manually injecting..
    ServiceImpl service; //Never use Interface here with @InjectMocks because its object cant be created

    @Mock //Creates Mock Object (NOT Spring Bean)
    CustomerRepository repository;

    

    @Test
    public void testGetCustomerById(){

        Customer cust = new Customer(); cust.setFirstName("Player"); cust.setLastName("John");

        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(cust));

        Customer result = service.getCustomerById(10L);

        System.out.println( result.getFirstName()  +" "+  result.getLastName());

        assertNotNull(result);
        assertEquals("Player", result.getFirstName());
    }
}