package com.example.restaurant_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnore  // Prevents infinite recursion
    private Restaurant ownedRestaurant;  

    @Column(nullable = false)
    private double walletBalance = 1000.0;  // Default balance on registration 

    public User() {}

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.walletBalance = 1000.0; // Default credits given on signup 
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Restaurant getOwnedRestaurant() { return ownedRestaurant; }
    public void setOwnedRestaurant(Restaurant ownedRestaurant) { this.ownedRestaurant = ownedRestaurant; }

    public double getWalletBalance() { return walletBalance; }  // Getter for wallet balance
    public void setWalletBalance(double walletBalance) { this.walletBalance = walletBalance; }  // ✅ Setter for wallet balance
}
