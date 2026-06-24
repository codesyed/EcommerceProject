package com.ecommerce.project.controller;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderRequestDTO;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private OrderService orderService;


    /*
        * Who calls the api placeOrder() POST-/orders API and where do payment & payment Gateway data come from?
            Nobody manually writes: POST /order/users/payments/UPI

            In Production Usually:

            Frontend (React/Angular)
                    ↓
            User clicks "Place Order"
                    ↓
            Payment Gateway Called
                    ↓
            Frontend receives payment response
                    ↓
            Frontend calls this backend placeOrder POST-/orders API.

            Example: User pays using Razorpay.

            Razorpay returns:
            {
               "paymentId":"pay_123",
               "status":"SUCCESS"
            }
            Frontend creates:
            {
               "addressId":1,
               "pgName":"RAZORPAY",
               "pgPaymentId":"pay_123",
               "pgStatus":"SUCCESS",
               "pgResponseMessage":"Payment Successful"
            }
     */
    @PostMapping("/order")
    public ResponseEntity<?> orderProducts(@RequestBody OrderRequestDTO orderRequestDTO){

        String emailId = authUtil.loggedInEmail();
        OrderDTO orderDTO = orderService.placeOrder(
          emailId,
          orderRequestDTO.getPaymentMethod(),
          orderRequestDTO.getAddressId(),
          orderRequestDTO.getPgName(),
          orderRequestDTO.getPgPaymentId(),
          orderRequestDTO.getPgStatus(),
          orderRequestDTO.getPgResponseMessage()
        );
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
        /*
        Tutor doesn't want service layer to depend on DTO, This is actually a valid architecture reason.
            Some companies do:
            Controller > DTO : Extract values > Service
            because: DTO belongs to API layer
            Service belongs to Business layer
            They don't want: Business Layer to  Depends on Web/API Layer
         */
    }
}
