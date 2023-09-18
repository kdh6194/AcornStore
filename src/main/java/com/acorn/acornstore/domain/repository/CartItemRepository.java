package com.acorn.acornstore.domain.repository;

import com.acorn.acornstore.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void delete(CartItem itemToRemove);
}
