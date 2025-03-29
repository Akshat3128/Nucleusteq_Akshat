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

    @Query("SELECT o FROM Order o WHERE  o.status NOT IN (:delivered, :cancelled)")
    List<Order> findActiveOrders(
        @Param("delivered") OrderStatus delivered, 
        @Param("cancelled") OrderStatus cancelled
    );

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.status NOT IN (:delivered, :cancelled)")
    Order findActiveOrdersById(
        @Param("customerId") Long customerId,
        @Param("delivered") OrderStatus delivered, 
        @Param("cancelled") OrderStatus cancelled
    );
    @Query("SELECT o FROM Order o WHERE o.id = :orderId ORDER BY o.orderTime DESC")
    List<Order> findByOrderIdSorted(@Param("orderId") Long orderId);

    // `orderTime` to take note of order creation time
    Optional<Order> findTopByCustomerOrderByOrderTimeDesc(User customer);
    
    List<Order> findByCustomerAndStatus(User customer, OrderStatus status);

}

