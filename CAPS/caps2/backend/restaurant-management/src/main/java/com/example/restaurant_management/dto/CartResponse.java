package com.example.restaurant_management.dto;

import com.example.restaurant_management.model.Cart;
import com.example.restaurant_management.model.CartItem;
import java.util.Set;

public class CartResponse {
    private Long cartId;
    private Set<CartItem> cartItems;
    private String restaurantName;

    // Constructor
    public CartResponse(Long cartId, Set<CartItem> cartItems, String restaurantName) {
        this.cartId = cartId;
        this.cartItems = cartItems;
        this.restaurantName = restaurantName;
    }

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Cart toCartEntity() {
        Cart cart = new Cart();
        cart.setId(this.cartId);
        cart.setCartItems(this.cartItems); 
        return cart;
    }

}
