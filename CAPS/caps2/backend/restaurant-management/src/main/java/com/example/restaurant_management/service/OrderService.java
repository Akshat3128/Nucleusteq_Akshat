package com.example.restaurant_management.service;

import com.example.restaurant_management.model.*;
import com.example.restaurant_management.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public OrderService(OrderRepository orderRepository, 
                        OrderItemRepository orderItemRepository,
                        UserRepository userRepository, 
                        WalletTransactionRepository walletTransactionRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    //  Place an Order (Ensures no duplicate pending orders)
    @Transactional
    public Order placeOrder(User customer, Restaurant restaurant, List<MenuItem> items, double totalPrice) {
        boolean hasPending = orderRepository.hasPendingOrders(customer.getId(), OrderStatus.DELIVERED, OrderStatus.CANCELLED);
        if (hasPending) {
            throw new RuntimeException("You cannot place a new order until the previous order is delivered or canceled.");
        }

        if (customer.getWalletBalance() < totalPrice) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        customer.setWalletBalance(customer.getWalletBalance() - totalPrice);
        userRepository.save(customer);

        Order newOrder = new Order(customer, restaurant, totalPrice, OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(newOrder);

        List<OrderItem> orderItems = items.stream().map(menuItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setMenuItem(menuItem);
            orderItem.setPrice(menuItem.getPrice());
            orderItem.setQuantity(1);
            return orderItem;
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        WalletTransaction transaction = new WalletTransaction(customer, -totalPrice, TransactionType.DEBIT, savedOrder);
        walletTransactionRepository.save(transaction);

        return savedOrder;
    }

    //  Mark an order as delivered
    // @Transactional
    public Order markOrderAsDelivered(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order not found!");
        }

        Order order = optionalOrder.get();
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order is not pending. It cannot be marked as delivered.");
        }

        order.setStatus(OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }

    // //  Mark an order as cancelled
    // @Transactional
    public Order markOrderAsCancelled(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order not found!");
        }

        Order order = optionalOrder.get();
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot cancel an order that is already delivered or cancelled.");
        }

        User customer = order.getCustomer();
        double refundedAmount = order.getTotalPrice();
        customer.setWalletBalance(customer.getWalletBalance() + refundedAmount);
        userRepository.save(customer);

        WalletTransaction refundTransaction = new WalletTransaction(customer, refundedAmount, TransactionType.CREDIT, order);
        walletTransactionRepository.save(refundTransaction);

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomer(User customer) {
        return orderRepository.findByCustomer(customer);
    }
    @Transactional(readOnly = true)
    public List<Order> getOrdersByRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }
}

