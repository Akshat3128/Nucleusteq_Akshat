package com.example.restaurant_management.service;

import com.example.restaurant_management.dto.CartResponse;
import com.example.restaurant_management.model.*;
import com.example.restaurant_management.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       RestaurantRepository restaurantRepository,
                       MenuItemRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    //  Add item to cart (Ensuring item belongs to restaurant)
    @Transactional
    public Cart addItemToCart(User customer, Long restaurantId, Long menuItemId, int quantity) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found!"));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found!"));

        //  Ensure menu item is in the restaurant's menu
        if (!restaurant.getMenuItems().contains(menuItem)) {
            throw new RuntimeException("This menu item is not available in the selected restaurant.");
        }

        //  Ensure user does not have items from multiple restaurants
        Optional<Cart> existingCart = cartRepository.findByCustomer(customer);
        if (existingCart.isPresent() && !existingCart.get().getRestaurant().equals(restaurant)) {
            throw new RuntimeException("You cannot add items from different restaurants to the cart.");
        }

        //  Add item to cart
        Cart cart = existingCart.orElseGet(() -> new Cart(customer, restaurant, new HashSet<>()));

        // Check if item already exists in the cart
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenuItem().equals(menuItem))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            cartItemRepository.save(existingItem.get()); //  Update item in repository
        } else {
            CartItem newItem = new CartItem(cart, menuItem, quantity, menuItem.getPrice());
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem); // Save new cart item
        }

        return cartRepository.save(cart);
    }

    //  Remove item from cart
    @Transactional
    public Cart removeItemFromCart(User customer, Long menuItemId) {
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart is empty!"));

        cart.getCartItems().removeIf(item -> item.getMenuItem().getId().equals(menuItemId));

        return cartRepository.save(cart);
    }

    // Update quantity of an item in cart
    @Transactional
    public Cart updateCartItemQuantity(User customer, Long menuItemId, int quantity) {
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found!"));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenuItem().getId().equals(menuItemId))
                .findFirst();

        if (existingItem.isEmpty()) {
            throw new RuntimeException("Item not found in cart!");
        }

        CartItem cartItem = existingItem.get();
        if (quantity <= 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem); 
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem); 
        }

        return cartRepository.save(cart);
    }

    //  Clear cart after placing an order
    @Transactional
    public void clearCart(User customer) {
        Optional<Cart> cart = cartRepository.findByCustomer(customer);
        cart.ifPresent(c -> {
            cartItemRepository.deleteAll(c.getCartItems()); //  Clear all items before deleting cart
            cartRepository.delete(c);
        });
    }

    //  Get cart by customer (Returns DTO instead of full entity)
    @Transactional(readOnly=true)
    public CartResponse getCartByCustomer(User customer) {
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found!"));

        //  Force loading of lazy collections
        cart.getCartItems().size();

        return new CartResponse(cart.getId(), cart.getCartItems(), cart.getRestaurant().getName());
    }

}
