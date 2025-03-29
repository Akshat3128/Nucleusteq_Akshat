package com.example.restaurant_management.dto;

import com.example.restaurant_management.model.MenuItem;
import java.util.List;

public class OrderRequest {
    private Long customerId;
    private Long restaurantId;
    private List<MenuItem> items;
    private double totalPrice;

    // Getters and Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
