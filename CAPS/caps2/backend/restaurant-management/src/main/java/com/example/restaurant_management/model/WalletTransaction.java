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

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public WalletTransaction() {
        this.timestamp = LocalDateTime.now();
    }

    public WalletTransaction(User user, double amount, TransactionType transactionType) {
        this.user = user;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public double getAmount() { return amount; }
    public TransactionType getTransactionType() { return transactionType; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
