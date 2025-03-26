package com.example.restaurant_management.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @ManyToMany(mappedBy = "menuItems")
    private List<Restaurant> restaurants;

    public MenuItem() {}

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() { 
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public double getPrice() { 
        return price; 
    }
    public void setPrice(double price) { 
        this.price = price; 
    }

    public List<Restaurant> getRestaurants() { 
        return restaurants; 
    }
    public void setRestaurants(List<Restaurant> restaurants) { 
        this.restaurants = restaurants; 
    }
}
