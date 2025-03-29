package com.example.restaurant_management.dto;

public class CartRequest {
    private Long userId;
    private Long restaurantId;
    private Long menuItemId;
    private int quantity;

    // Constructors
    public CartRequest() {}

    public CartRequest(Long userId, Long restaurantId, Long menuItemId, int quantity) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
