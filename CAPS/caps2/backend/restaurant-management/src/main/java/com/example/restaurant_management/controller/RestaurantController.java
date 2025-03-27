package com.example.restaurant_management.controller;

import com.example.restaurant_management.model.Restaurant;
import com.example.restaurant_management.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // Created a new restaurant (Only RESTAURANT_OWNER can create & only one per owner)
    @PostMapping("/create")
    public ResponseEntity<?> createRestaurant(@RequestBody Map<String, String> request) {
        try {
            Long userId = Long.parseLong(request.get("userId"));  
            String restaurantName = request.get("name");

            Restaurant restaurant = restaurantService.createRestaurant(restaurantName, userId);
            return ResponseEntity.ok(Map.of("message", "Restaurant created successfully!", "restaurant", restaurant));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Unexpected error occurred: " + e.getMessage()));
        }
    }

    // Get all restaurants
    @GetMapping("/all")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    // Get a specific restaurant by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    //  Update restaurant name (Only owner can update)
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String newName = request.get("name");
            Long userId = Long.parseLong(request.get("userId"));

            Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, newName, userId);
            return ResponseEntity.ok(Map.of("message", "Restaurant updated successfully!", "restaurant", updatedRestaurant));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Unexpected error occurred: " + e.getMessage()));
        }
    }

    // Soft delete a restaurant (Mark as inactive instead of deleting)
    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateRestaurant(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            Long userId = Long.parseLong(request.get("userId"));  // Extract user ID
            restaurantService.deactivateRestaurant(id, userId);
            return ResponseEntity.ok(Map.of("message", "Restaurant deactivated successfully!"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Unexpected error occurred: " + e.getMessage()));
        }
    }

}
