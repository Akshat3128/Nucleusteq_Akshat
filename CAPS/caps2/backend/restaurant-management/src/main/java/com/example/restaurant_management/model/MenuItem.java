package com.example.restaurant_management.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private boolean available = true;

    @ManyToMany(mappedBy = "menuItems", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Restaurant> restaurants;

    public MenuItem() {}

    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = true;
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

    public String getCategory() { return category; }
    public void setCategory(String category) { 
        this.category = category; 
    }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available){ 
        this.available = available; 
    }
    public List<Restaurant> getRestaurants() { 
        return restaurants; 
    }
    public void setRestaurants(List<Restaurant> restaurants) { 
        this.restaurants = restaurants; 
    }
}
