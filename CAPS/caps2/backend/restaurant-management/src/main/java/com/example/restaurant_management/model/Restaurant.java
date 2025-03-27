package com.example.restaurant_management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    @JsonBackReference  // Prevents infinite recursion with User
    private User owner;


    @ManyToMany
    @JoinTable(
        name = "restaurant_menu_items",
        joinColumns = @JoinColumn(name = "restaurant_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    @JsonIgnore
    private List<MenuItem> menuItems = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @Column(nullable = false)
    private boolean active = true;  // ✅ Soft delete support

    //  Default Constructor
    public Restaurant() {
        this.menuItems = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.active = true;
    }

    //  Constructor with parameters
    public Restaurant(String name, User owner) {
        this.name = name;
        this.owner = owner;
        this.menuItems = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.active = true;
    }

    //  Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public List<MenuItem> getMenuItems() { return menuItems; }
    public void setMenuItems(List<MenuItem> menuItems) { this.menuItems = menuItems; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; } 

    
    @Override
    public String toString() {
        return "Restaurant{id=" + id + ", name='" + name + "', owner=" + owner.getEmail() + ", active=" + active + "}";
    }
}
