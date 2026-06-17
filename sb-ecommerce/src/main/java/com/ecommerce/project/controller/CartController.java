package com.ecommerce.project.controller;

import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    CartRepository cartRepository;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<?> addProductToUsersCart(@PathVariable Long productId,
                                                   @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToUserCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
     }

    @GetMapping("/carts")
    public ResponseEntity<?> getAllCarts(){
        List<CartDTO> cartDTOList = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOList, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<?> getLoggedInUserCart(){

        /*
            -> Using both email and cartId to find Cart Not cartId only
            -> Because of some design decisions
            -> Although our code and architecture may work on cartId only
                but that's not a scalable way.
            -> It is preferred to use email in design architecture to access unique things belongs to user.
         */
        String emailId=authUtil.loggedInEmail();

        Cart cart = cartRepository.findCartByUserEmail(emailId);
        if(cart==null)
            throw new ResourceNotFoundException("Cart", "email", emailId);

        Long cartId = cart.getCartId();

        CartDTO cartDTO = cartService.getLoggedInUserCart(emailId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }


    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<?> updateProductOfCart(
            @PathVariable Long productId,
            @PathVariable String operation
    ){
        //operation = delete then updateQuantity = -1 Otherwise + 1
        Integer updateQuantity =
                operation.equalsIgnoreCase("delete")?-1:1;
        CartDTO cartDTO = cartService.updateProductQuantityInUserCart(productId, updateQuantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }


}
