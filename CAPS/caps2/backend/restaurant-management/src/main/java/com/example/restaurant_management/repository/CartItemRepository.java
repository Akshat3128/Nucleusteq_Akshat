package com.example.restaurant_management.repository;

import com.example.restaurant_management.model.Cart;
import com.example.restaurant_management.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
}
