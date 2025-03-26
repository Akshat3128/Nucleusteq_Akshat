package com.example.restaurant_management.model;

public enum OrderStatus {
    PENDING, // Order placed but not yet processed
    PREPARING, // Order is being prepared
    COMPLETED, // Order is ready for pickup/delivery
    DELIVERED, // Order has been delivered
    CANCELLED  // Order was cancelled
}
