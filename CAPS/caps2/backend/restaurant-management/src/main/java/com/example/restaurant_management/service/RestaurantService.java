package com.example.restaurant_management.service;

import com.example.restaurant_management.model.MenuItem;
import com.example.restaurant_management.model.Restaurant;
import com.example.restaurant_management.model.User;
import com.example.restaurant_management.repository.MenuItemRepository;
import com.example.restaurant_management.repository.RestaurantRepository;
import com.example.restaurant_management.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
// import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);
    @Autowired
    private final RestaurantRepository restaurantRepository;
    @Autowired
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }

    // Create a new restaurant (Only one per owner)
    public Restaurant createRestaurant(String name, Long ownerId) {
        try {
            if (restaurantRepository.existsByOwnerId(ownerId)) {
                throw new RuntimeException("User already owns a restaurant!");
            }
    
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
    
            Restaurant restaurant = new Restaurant(name, owner);
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            logger.info("Restaurant created: {}", savedRestaurant);
            return savedRestaurant;
        } catch (Exception e) {
            logger.error("Error creating restaurant: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating restaurant. Please try again.");
        }
    }

    // Get all restaurants
    public List<Restaurant> getAllRestaurants() {
        try {
            return restaurantRepository.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving restaurants: {}", e.getMessage(), e);
            throw new RuntimeException("Could not fetch restaurants.");
        }
    }

    // Get a specific restaurant by ID
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    // Update restaurant name (Only owner can update)
    public Restaurant updateRestaurant(Long restaurantId, String newName, Long userId) {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            // Ensure the user is the owner of the restaurant
            if (!restaurant.getOwner().getId().equals(userId)) {
                throw new RuntimeException("You are not the owner of this restaurant.");
            }

            restaurant.setName(newName);
            Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
            logger.info("Restaurant updated: {}", updatedRestaurant);
            return updatedRestaurant;
        } catch (Exception e) {
            logger.error("Error updating restaurant: {}", e.getMessage(), e);
            throw new RuntimeException("Could not update restaurant.");
        }
    }

    // Soft delete (Deactivate restaurant)
    public void deactivateRestaurant(Long restaurantId, Long userId) {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            // Ensure the user is the owner of the restaurant
            if (!restaurant.getOwner().getId().equals(userId)) {
                throw new RuntimeException("You are not the owner of this restaurant.");
            }

            restaurant.setActive(false);
            restaurantRepository.save(restaurant);
            logger.info("Restaurant deactivated.");
        } catch (Exception e) {
            logger.error("Error deactivating restaurant: {}", e.getMessage(), e);
            throw new RuntimeException("Could not deactivate restaurant.");
        }
    }

    @Transactional  //  Ensures Hibernate session remains open
    public Restaurant addMenuItem(Long restaurantId, Long menuItemId, Long userId) {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            //  Ensure the user is the owner of the restaurant
            if (!restaurant.getOwner().getId().equals(userId)) {
                throw new RuntimeException("You are not the owner of this restaurant.");
            }

            MenuItem menuItem = menuItemRepository.findById(menuItemId)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            //  Ensure Hibernate initializes the menuItems collection before modifying it
            restaurant.getMenuItems().size();  // Forces lazy loading

            //  Add menu item
            restaurant.getMenuItems().add(menuItem);
            restaurantRepository.save(restaurant);

            logger.info("Menu item {} added to restaurant {}", menuItemId, restaurantId);
            return restaurant;
        } catch (Exception e) {
            logger.error("Error adding menu item to restaurant: {}", e.getMessage(), e);
            throw new RuntimeException("Could not assign menu items to restaurant.");
        }
    }

    //  Remove menu items from a restaurant
    @Transactional
    public void removeMenuItem(Long restaurantId, Long menuItemId, Long userId) {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            // Ensure the user is the owner of the restaurant
            if (!restaurant.getOwner().getId().equals(userId)) {
                throw new RuntimeException("You are not the owner of this restaurant.");
            }

            MenuItem menuItem = menuItemRepository.findById(menuItemId)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            //  Initialize menuItems before modifying
            restaurant.getMenuItems().size();  // Forces Hibernate to load menuItems

            restaurant.getMenuItems().remove(menuItem);
            restaurantRepository.save(restaurant);
            logger.info("Menu item removed from restaurant.");
        } catch (Exception e) {
            logger.error("Error removing menu items from restaurant: {}", e.getMessage(), e);
            throw new RuntimeException("Could not remove menu item from restaurant.");
        }
    }

    //  Get all menu items of a restaurant
    @Transactional  //  Keeps session open while fetching menuItems
    public Set<MenuItem> getMenuItems(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Explicitly load the collection before returning it
        restaurant.getMenuItems().size();  //  Ensures menuItems is initialized
        return restaurant.getMenuItems();
    }
}
