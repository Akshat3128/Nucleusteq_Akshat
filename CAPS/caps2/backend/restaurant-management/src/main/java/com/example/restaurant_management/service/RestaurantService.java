package com.example.restaurant_management.service;

import com.example.restaurant_management.model.Restaurant;
import com.example.restaurant_management.model.Role;
import com.example.restaurant_management.model.User;
import com.example.restaurant_management.repository.RestaurantRepository;
import com.example.restaurant_management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
// import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    // ✅ Create a restaurant (Only users with RESTAURANT_OWNER role can create)
    public Restaurant createRestaurant(String name, Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found."));

            if (!user.getRole().equals(Role.RESTAURANT_OWNER)) {
                throw new RuntimeException("Only users with RESTAURANT_OWNER role can create a restaurant.");
            }

            // ✅ Ensure a user doesn't create multiple restaurants
            if (restaurantRepository.findByOwner(user).isPresent()) {
                throw new RuntimeException("User already owns a restaurant.");
            }

            Restaurant restaurant = new Restaurant(name, user);
            return restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new RuntimeException("Error creating restaurant: " + e.getMessage());
        }
    }

    // ✅ Get all restaurants
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // ✅ Get a specific restaurant by ID
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant with ID " + id + " not found."));
    }

    // ✅ Update restaurant name (Only the assigned restaurant owner can update)
    public Restaurant updateRestaurant(Long id, String newName, Long userId) {
        try {
            Restaurant restaurant = getRestaurantById(id);

            if (!restaurant.getOwner().getId().equals(userId)) {
                throw new RuntimeException("You are not authorized to update this restaurant.");
            }

            restaurant.setName(newName);
            return restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new RuntimeException("Error updating restaurant: " + e.getMessage());
        }
    }

    // ✅ Soft delete a restaurant (Mark as inactive instead of deleting)
    public void deactivateRestaurant(Long id, Long userId) {
        try {
            Restaurant restaurant = getRestaurantById(id);

            if (!restaurant.getOwner().getId().equals(userId)) {
                throw new RuntimeException("You are not authorized to deactivate this restaurant.");
            }

            restaurant.setActive(false);
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new RuntimeException("Error deactivating restaurant: " + e.getMessage());
        }
    }
}
