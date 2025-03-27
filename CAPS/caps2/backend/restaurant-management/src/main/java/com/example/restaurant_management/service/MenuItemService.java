package com.example.restaurant_management.service;

import com.example.restaurant_management.model.MenuItem;
import com.example.restaurant_management.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemService.class);
    private final MenuItemRepository menuItemRepository;


    
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    // ✅ Create a menu item with logging and error handling
    public MenuItem createMenuItem(MenuItem menuItem) {
        try {
            MenuItem savedItem = menuItemRepository.save(menuItem);
            logger.info("Menu item created: {}", savedItem);
            return savedItem;
        } catch (Exception e) {
            logger.error("Error creating menu item: {}", e.getMessage());
            throw new RuntimeException("Error creating menu item. Please try again.");
        }
    }

    // ✅ Get all available menu items with logging
    public List<MenuItem> getAllMenuItems() {
        try {
            List<MenuItem> menuItems = menuItemRepository.findByAvailableTrue();
            logger.info("Retrieved {} available menu items", menuItems.size());
            return menuItems;
        } catch (Exception e) {
            logger.error("Error retrieving menu items: {}", e.getMessage());
            throw new RuntimeException("Could not fetch menu items. Please try again.");
        }
    }

    // ✅ Get a menu item by ID with logging
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Menu item not found for ID: {}", id);
                    return new RuntimeException("Menu item not found for ID: " + id);
                });
    }

    // ✅ Update menu item with logging
    public MenuItem updateMenuItem(Long id, String newName, double newPrice, String newCategory, boolean available) {
        try {
            MenuItem menuItem = getMenuItemById(id);
            menuItem.setName(newName);
            menuItem.setPrice(newPrice);
            menuItem.setCategory(newCategory);
            menuItem.setAvailable(available);
            MenuItem updatedItem = menuItemRepository.save(menuItem);
            logger.info("Menu item updated: {}", updatedItem);
            return updatedItem;
        } catch (Exception e) {
            logger.error("Error updating menu item (ID: {}): {}", id, e.getMessage());
            throw new RuntimeException("Could not update menu item. Please try again.");
        }
    }

    // ✅ Mark a menu item as unavailable instead of deleting
    public void markMenuItemAsUnavailable(Long id) {
        try {
            MenuItem menuItem = getMenuItemById(id);
            menuItem.setAvailable(false);
            menuItemRepository.save(menuItem);
            logger.info("Menu item (ID: {}) marked as unavailable", id);
        } catch (Exception e) {
            logger.error("Error marking menu item as unavailable (ID: {}): {}", id, e.getMessage());
            throw new RuntimeException("Could not mark menu item as unavailable.");
        }
    }

    // ✅ Get menu items by category with logging
    public List<MenuItem> getMenuItemsByCategory(String category) {
        try {
            List<MenuItem> menuItems = menuItemRepository.findByCategoryAndAvailableTrue(category);
            logger.info("Retrieved {} menu items in category: {}", menuItems.size(), category);
            return menuItems;
        } catch (Exception e) {
            logger.error("Error retrieving menu items by category: {}", e.getMessage());
            throw new RuntimeException("Could not fetch menu items for the given category.");
        }
    }
}
