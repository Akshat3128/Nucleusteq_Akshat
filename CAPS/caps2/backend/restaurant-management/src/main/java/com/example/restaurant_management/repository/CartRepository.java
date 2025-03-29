package com.example.restaurant_management.repository;

import com.example.restaurant_management.model.Cart;
import com.example.restaurant_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomer(User customer);
    void deleteByCustomer(User customer);
}
