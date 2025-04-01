package com.example.restaurant_management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // A single owner can have only one restaurant
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    @JsonBackReference
    private User owner;

    // Many-to-Many relationship with MenuItem
    @ManyToMany
    @JoinTable(
        name = "restaurant_menu_item",
        joinColumns = @JoinColumn(name = "restaurant_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    @JsonIgnore    
    private Set<MenuItem> menuItems;

    // One-to-Many relationship with Orders
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Order> orders;

    @Column(nullable = false)
    private boolean active = true;

    // Constructors
    public Restaurant() {
        this.menuItems = new HashSet<>();
        this.orders = new ArrayList<>();
        this.active = true;
    }

    public Restaurant(String name, User owner) {
        this.name = name;
        this.owner = owner;
        this.menuItems = new HashSet<>();
        this.orders = new ArrayList<>();
        this.active = true;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public Set<MenuItem> getMenuItems() { return menuItems; }
    public void setMenuItems(Set<MenuItem> menuItems) { this.menuItems = menuItems; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // Utility Methods
    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }

    public void removeMenuItem(MenuItem menuItem) {
        this.menuItems.remove(menuItem);
    }

    @Override
    public String toString() {
        return "Restaurant{id=" + id + ", name='" + name + "', owner=" + (owner != null ? owner.getEmail() : "null") + ", active=" + active + "}";
    }
}
