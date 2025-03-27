package com.example.restaurant_management.repository;

import com.example.restaurant_management.model.Restaurant;
import com.example.restaurant_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByOwner(User owner); // checking existing restaurant
}
