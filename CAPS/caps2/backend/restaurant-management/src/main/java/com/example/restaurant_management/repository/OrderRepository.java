package com.example.restaurant_management.repository;

import com.example.restaurant_management.model.Order;
import com.example.restaurant_management.model.OrderStatus;
import com.example.restaurant_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 🔹 Find all orders by customer
    List<Order> findByCustomer(User customer);

    // 🔹 Find all orders for a specific restaurant
    List<Order> findByRestaurantId(Long restaurantId);

    
    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.customer.id = :customerId AND o.status NOT IN (:delivered, :cancelled)")
    boolean hasPendingOrders(
        @Param("customerId") Long customerId,
        @Param("delivered") OrderStatus delivered,
        @Param("cancelled") OrderStatus cancelled
    );

    // `orderTime` to take note of order creation time
    Optional<Order> findTopByCustomerOrderByOrderTimeDesc(User customer);
}
