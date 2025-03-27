package com.example.restaurant_management.controller;

import com.example.restaurant_management.model.MenuItem;
import com.example.restaurant_management.service.MenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // ✅ Create a new menu item
    @PostMapping("/create")
    public ResponseEntity<?> createMenuItem(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            double price = Double.parseDouble(request.get("price"));
            String category = request.get("category");
            boolean available = Boolean.parseBoolean(request.get("available"));

            MenuItem menuItem = new MenuItem(name, price, category);
            menuItem.setAvailable(available);

            return ResponseEntity.ok(menuItemService.createMenuItem(menuItem));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid price format"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get all menu items
    @GetMapping("/all")
    public ResponseEntity<?> getAllMenuItems() {
        try {
            return ResponseEntity.ok(menuItemService.getAllMenuItems());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get menu item by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMenuItemById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(menuItemService.getMenuItemById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Update menu item
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String newName = request.get("name");
            double newPrice = Double.parseDouble(request.get("price"));
            String newCategory = request.get("category");
            boolean available = Boolean.parseBoolean(request.get("available"));

            MenuItem updatedMenuItem = menuItemService.updateMenuItem(id, newName, newPrice, newCategory, available);
            return ResponseEntity.ok(Map.of("message", "Menu item updated successfully!", "menuItem", updatedMenuItem));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid price format"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Soft delete (mark as unavailable)
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        try {
            menuItemService.markMenuItemAsUnavailable(id);
            return ResponseEntity.ok(Map.of("message", "Menu item marked as unavailable!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get menu items by category
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getMenuItemsByCategory(@PathVariable String category) {
        try {
            return ResponseEntity.ok(menuItemService.getMenuItemsByCategory(category));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
