package com.example.restaurant_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_transactions")
public class WalletTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  //Transaction belongs to a user

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;  // CREDIT or DEBIT

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true)  // Order is optional
    private Order order;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public WalletTransaction() {
        this.timestamp = LocalDateTime.now();
    }

    // ✅ Existing constructor (Without Order)
    public WalletTransaction(User user, double amount, TransactionType transactionType) {
        this.user = user;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();
    }

    // ✅ New Constructor (With Order)
    public WalletTransaction(User user, double amount, TransactionType transactionType, Order order) {
        this.user = user;
        this.amount = amount;
        this.transactionType = transactionType;
        this.order = order;
        this.timestamp = LocalDateTime.now();
    }

    // 🔹 Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public double getAmount() { return amount; }
    public TransactionType getTransactionType() { return transactionType; }
    public Order getOrder() { return order; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
