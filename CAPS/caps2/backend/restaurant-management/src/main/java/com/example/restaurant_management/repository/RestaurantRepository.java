package com.example.restaurant_management.repository;

import com.example.restaurant_management.model.MenuItem;
import com.example.restaurant_management.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    //  Find a restaurant by ownerId (To ensure a single owner has only one restaurant)
    Optional<Restaurant> findByOwnerId(Long ownerId);

    // Checking if a restaurant exists by ownerId (To prevent duplicate ownership)
    boolean existsByOwnerId(Long ownerId);

    Restaurant findByMenuItemsContaining(@Param("menuItem") MenuItem menuItem);

    // List<Restaurant> findAll();
}
