package com.acorn.acornstore.domain.repository;

import com.acorn.acornstore.domain.CartItem;

public interface CartItemRepository {
    void delete(CartItem itemToRemove);
}
