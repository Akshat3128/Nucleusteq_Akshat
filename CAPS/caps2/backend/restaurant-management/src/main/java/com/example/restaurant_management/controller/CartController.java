package com.example.restaurant_management.controller;

import com.example.restaurant_management.dto.CartRequest;
import com.example.restaurant_management.dto.CartResponse;
import com.example.restaurant_management.dto.UpdateCartItemRequest;
import com.example.restaurant_management.model.Cart;
import com.example.restaurant_management.model.User;
import com.example.restaurant_management.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //  Add item to cart
    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody CartRequest request) {
        try {
            User user = new User();
            user.setId(request.getUserId());

            Cart updatedCart = cartService.addItemToCart(user, request.getRestaurantId(), request.getMenuItemId(), request.getQuantity());
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  Remove item from cart
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestBody UpdateCartItemRequest request) {
        try {
            User user = new User();
            user.setId(request.getUserId());

            Cart updatedCart = cartService.removeItemFromCart(user, request.getMenuItemId());
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  Update item quantity in cart
    @PutMapping("/update")
    public ResponseEntity<?> updateCartItemQuantity(@RequestBody UpdateCartItemRequest request) {
        try {
            User user = new User();
            user.setId(request.getUserId());

            Cart updatedCart = cartService.updateCartItemQuantity(user, request.getMenuItemId(), request.getQuantity());
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) { 
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while updating cart item quantity.");
        }
    }

    //  Get cart by customer (Uses DTO)
    @GetMapping("/view/{userId}")  
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        try {
            User user = new User();
            user.setId(userId);

            CartResponse cartResponse = cartService.getCartByCustomer(user);
            return ResponseEntity.ok(cartResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestBody UpdateCartItemRequest request) {
        try {
            User user = new User();
            user.setId(request.getUserId());

            cartService.clearCart(user);
            return ResponseEntity.ok("Cart cleared successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
