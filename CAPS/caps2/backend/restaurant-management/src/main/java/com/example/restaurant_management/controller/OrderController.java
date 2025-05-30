package com.example.restaurant_management.controller;

import com.example.restaurant_management.dto.OrderRequest;
import com.example.restaurant_management.model.*;
import com.example.restaurant_management.service.OrderService;
import com.example.restaurant_management.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    //  Place an Order (Now fetches items from the cart)
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest) {
        // Fetch customer
        Optional<User> customerOpt = userRepository.findById(orderRequest.getCustomerId());
        if (customerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Customer not found.");
        }
        User customer = customerOpt.get();

        try {
            Order order = orderService.placeOrderFromCart(customer); 
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  Mark Order as Delivered
    @PutMapping("/{orderId}/delivered")
    public ResponseEntity<?> markOrderAsDelivered(@PathVariable Long orderId) {
        try {
            Order order = orderService.markOrderAsDelivered(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cancel an Order
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> markOrderAsCancelled(@PathVariable Long orderId) {
        try {
            Order order = orderService.markOrderAsCancelled(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get Orders for a Customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Long customerId) {
        Optional<User> customerOpt = userRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        List<Order> orders = orderService.getOrdersByCustomer(customerOpt.get());
        return ResponseEntity.ok(orders);
    }

    //  Get Orders for a Restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        List<Order> orders = orderService.getOrdersByRestaurant(restaurantId);
        return ResponseEntity.ok(orders);
    }
}
