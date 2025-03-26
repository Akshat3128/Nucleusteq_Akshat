package com.example.restaurant_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; 

    public Order() {
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.PENDING; // Default status
    }

    public Order(User customer, Restaurant restaurant, OrderStatus status) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.orderTime = LocalDateTime.now();
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { 
        this.restaurant = restaurant; 
    }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }

    public OrderStatus getStatus() { 
        return status; 
    }
    public void setStatus(OrderStatus status) { 
        this.status = status;
    }
}
