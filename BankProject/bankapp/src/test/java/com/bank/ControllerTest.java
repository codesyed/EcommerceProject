package com.bank;

import com.bank.controller.CustomerController;
import com.bank.customer.Customer;
import com.bank.service.ServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//-> Loads ONLY Controller/Web Layer (Inside SpringContext)
//-> Does NOT load full Spring Application
//-> Faster Controller/API Testing
@WebMvcTest(CustomerController.class)
public class ControllerTest {

    //No Need of Controller Dependency
    //MockMvc: Used to simulate HTTP request WITHOUT starting actual server
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ServiceImpl service; //To be automatically injected inside Controller created by MockMvc

    @Autowired
    ObjectMapper objectMapper; //For Json to JavaObject and ViceVersa Used in Post Method Testing

    @Test
    public void testGetCustomerById() throws Exception {
        Customer dummy= new Customer(); dummy.setFirstName("Abdullah"); dummy.setLastName("Affan");

        //What when service class 'getCustomerById' is called!
        when(service.getCustomerById(anyLong()))
                .thenReturn(dummy);

        //Actually calling controller EndPoint
        //-> Simulates GET Request using 'MockMvc'
        mockMvc.perform(get("/api/customers/{id}", 9999))
                .andExpect(status().isOk()) //Check HttpStatus code=200
                .andExpect(
                        jsonPath("$.firstName")
                        .value("Abdullah")
                )
                .andExpect(
                        jsonPath("$.lastName")
                        .value("Affan")
                        //Checks JSON field: firstName == "Affan"
                );

        /*
        In MockMvc/API Controller Testing,
        .andExpect() itself works as Assertion/Validation,
        so generally separate assertEquals()/assertNotNull() methods are not needed. */
    }

    @Test
    public void createCustomerTest() throws Exception {
        Customer dummy=new Customer(); dummy.setFirstName("Syed"); dummy.setLastName("Yusuf");

        //What when service class 'createCustomer' is called!
        when(service.createCustomer(any()))
                .thenReturn(dummy);

        //We Need Json as Controller Receives Json in Body
        String json = objectMapper.writeValueAsString(dummy);

        // Actually calling controller EndPoint
        //-> Simulates POST Request using 'MockMvc'
        //-> Sending Json in request
        mockMvc.perform(
                post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andExpect(
                jsonPath("$.firstName")
                        .value("Syed")
        );
    }
}

/*



    -> We need Controller Dependency - MockMvc will do this
    -> Inside Controller these needs Service Dependency therefore using @MockBean
    -> Not service bean got automatically inside Controller through MockMvc
    -> Now whhat when service repos method is called (using -> when()..then)



 */
