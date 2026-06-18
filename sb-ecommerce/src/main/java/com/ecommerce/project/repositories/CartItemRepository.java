package com.ecommerce.project.repositories;

import com.ecommerce.project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    //.productId .cartId or .id only?
    @Query("SELECT ci FROM CartItem ci WHERE ci.product.productId=?1 AND ci.cart.cartId = ?2")
    CartItem findCartItemByProductIdAndCartId(Long prodcutId, Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.product.productId = ?2")
    void deleteCartItemByCartIdAndProductId(Long cartId, Long productId);
}
