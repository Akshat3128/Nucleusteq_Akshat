package com.example.restaurant_management.service;

import com.example.restaurant_management.model.*;
import com.example.restaurant_management.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, 
                        OrderItemRepository orderItemRepository,
                        UserRepository userRepository, 
                        WalletTransactionRepository walletTransactionRepository,
                        CartRepository cartRepository,
                        CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Order placeOrderFromCart(User customer) {
        // Check existing pending orders
        boolean hasPending = orderRepository.hasPendingOrders(
                customer.getId(), OrderStatus.DELIVERED, OrderStatus.CANCELLED);
        if (hasPending) {
            throw new RuntimeException("Complete or cancel your pending order before placing a new one.");
        }

        // Fetch cart
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart is empty!"));

        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty! Add items before placing an order.");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice()) // 🔥 Fixed here
                .sum();

        //  Check wallet balance
        if (customer.getWalletBalance() < totalAmount) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        //  Deduct wallet balance
        customer.setWalletBalance(customer.getWalletBalance() - totalAmount);
        userRepository.save(customer);

        //  Create new order
        Order order = new Order(customer, cart.getRestaurant(), totalAmount, OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        // Convert cart items to order items
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setMenuItem(cartItem.getMenuItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice()); // 🔥 Fixed here
            return orderItem;
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        //  Save transaction & clear cart
        WalletTransaction transaction = new WalletTransaction(customer, -totalAmount, TransactionType.DEBIT, savedOrder);
        walletTransactionRepository.save(transaction);

        cartItemRepository.deleteAll(cartItems);
        cartRepository.delete(cart);

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

