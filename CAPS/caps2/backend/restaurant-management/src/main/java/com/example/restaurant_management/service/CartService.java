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

    @Transactional
    public Cart addItemToCart(User customer, Long restaurantId, Long menuItemId, int quantity) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found!"));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found!"));

        if (!restaurant.getMenuItems().contains(menuItem)) {
            throw new RuntimeException("This menu item is not available in the selected restaurant.");
        }

        Optional<Cart> existingCart = cartRepository.findByCustomer(customer);
        if (existingCart.isPresent() && !existingCart.get().getRestaurant().equals(restaurant)) {
            throw new RuntimeException("You cannot add items from multiple restaurants to the cart.");
        }

        Cart cart = existingCart.orElseGet(() -> new Cart(customer, restaurant, new HashSet<>()));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenuItem().equals(menuItem))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            cartItemRepository.save(existingItem.get());
        } else {
            CartItem newItem = new CartItem(cart, menuItem, quantity, menuItem.getPrice());
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItemFromCart(User customer, Long menuItemId) {
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart is empty!"));

        cart.getCartItems().removeIf(item -> item.getMenuItem().getId().equals(menuItemId));

        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(User customer) {
        Optional<Cart> cart = cartRepository.findByCustomer(customer);
        cart.ifPresent(c -> {
            cartItemRepository.deleteAll(c.getCartItems());
            cartRepository.delete(c);
        });
    }

    @Transactional(readOnly=true)
    public CartResponse getCartByCustomer(User customer) {
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found!"));

        cart.getCartItems().size();

        return new CartResponse(cart.getId(), cart.getCartItems(), cart.getRestaurant().getName());
    }

    @Transactional
    public Cart updateCartItemQuantity(User customer, Long menuItemId, int newQuantity) {
    // Fetch the cart
    Cart cart = cartRepository.findByCustomer(customer)
            .orElseThrow(() -> new RuntimeException("Cart not found for this customer."));

    // Find the cart item
    CartItem cartItem = cart.getCartItems().stream()
            .filter(item -> item.getMenuItem().getId().equals(menuItemId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Menu item not found in the cart."));

    // Update quantity
    if (newQuantity > 0) {
        cartItem.setQuantity(newQuantity);
    } else {
        // Remove the item if quantity is 0
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    // Save updated cart
    return cartRepository.save(cart);
}

}
