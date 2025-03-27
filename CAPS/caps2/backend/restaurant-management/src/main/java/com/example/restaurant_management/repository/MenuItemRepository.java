package com.example.restaurant_management.repository;

import com.example.restaurant_management.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    // ✅ Get all available menu items
    List<MenuItem> findByAvailableTrue();

    // ✅ Find available menu items by category
    List<MenuItem> findByCategoryAndAvailableTrue(String category);

    // ✅ Find available menu items by category sorted by price (Optional)
    List<MenuItem> findByCategoryAndAvailableTrueOrderByPriceAsc(String category);
}
