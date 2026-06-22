package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {


    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItemList ci JOIN FETCH ci.product p WHERE p.productId=?1")
    List<Cart> findCartsByProductId(Long productId);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    //Also try it with native query
    Cart findCartByUserEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email=?1 AND c.cartId=?2")
    Cart findCartByUserEmailAndCartId(String emailId, Long cartId);
}
