package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    CartDTO addProductToUserCart(Long prodcutId, Integer quantity);
    List<CartDTO> getAllCarts();

    CartDTO getLoggedInUserCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInUserCart(Long productId, Integer updateQuantity);

    @Transactional
    CartDTO deleteProductFromCart(Long productId);

    void updateCartItemInCart(Long cartId, Product product);

    @Transactional
    void deleteItemsFromCart(Long productId, Long cartId);
}
