package com.example.restaurant_management.service;

import com.example.restaurant_management.model.*;
import com.example.restaurant_management.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 🔹 Place a new order for a customer
    public Order placeOrder(User customer, Restaurant restaurant, List<MenuItem> items) {
        //  Ensure the customer has no pending orders
        boolean hasPending = orderRepository.hasPendingOrders(customer.getId(), OrderStatus.DELIVERED, OrderStatus.CANCELLED);
        if (hasPending) {
            throw new RuntimeException("You cannot place a new order until the previous order is delivered or canceled.");
        }

        // Create and save the new order
        Order newOrder = new Order(customer, restaurant, OrderStatus.PENDING);
        return orderRepository.save(newOrder);
    }

    // 🔹 Mark an order as delivered
    public Order markOrderAsDelivered(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.DELIVERED);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found!");
    }

    // 🔹 Get all orders for a customer
    public List<Order> getOrdersByCustomer(User customer) {
        return orderRepository.findByCustomer(customer);
    }

    // 🔹 Get all orders for a restaurant
    public List<Order> getOrdersByRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }
}
