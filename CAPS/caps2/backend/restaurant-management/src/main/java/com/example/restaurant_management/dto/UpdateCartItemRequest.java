package com.example.restaurant_management.dto;

public class UpdateCartItemRequest {
    private Long userId;
    private Long menuItemId;
    private int quantity;

    // Constructors
    public UpdateCartItemRequest() {}

    public UpdateCartItemRequest(Long userId, Long menuItemId, int quantity) {
        this.userId = userId;
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
