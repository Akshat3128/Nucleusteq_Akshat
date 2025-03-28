package com.example.restaurant_management.service;

import com.example.restaurant_management.model.*;
import com.example.restaurant_management.repository.UserRepository;
import com.example.restaurant_management.repository.WalletTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class WalletService {

    private final UserRepository userRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletService(UserRepository userRepository, WalletTransactionRepository walletTransactionRepository) {
        this.userRepository = userRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    //  1. Get Wallet Balance
    public double getWalletBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getWalletBalance();
    }

    //  2. Add Funds to Wallet
    @Transactional
    public WalletTransaction addFunds(Long userId, double amount) {
        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than zero.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double maxBalance = 5000.0;  // Max wallet balance limit
        double maxDepositPerTransaction = 50.0;  // Max deposit limit per transaction
        double dailyLimit = 100.0;  //Max deposit limit per day

        if (amount > maxDepositPerTransaction) {
            throw new RuntimeException("You can add a maximum of " + maxDepositPerTransaction + " credits per transaction.");
        }

        //To Check how much user has added today
        LocalDate today = LocalDate.now();
        List<WalletTransaction> todayTransactions = walletTransactionRepository.findByUserIdAndTimestampBetween(
            userId, today.atStartOfDay(), today.plusDays(1).atStartOfDay()
        );

        double totalAddedToday = todayTransactions.stream()
            .filter(t -> t.getTransactionType() == TransactionType.CREDIT)
            .mapToDouble(WalletTransaction::getAmount)
            .sum();

        if (totalAddedToday + amount > dailyLimit) {
            throw new RuntimeException("You can add a maximum of " + dailyLimit + " credits per day.");
        }

        double newBalance = user.getWalletBalance() + amount;
        if (newBalance > maxBalance) {
            throw new RuntimeException("Wallet balance cannot exceed " + maxBalance + " credits.");
        }

        user.setWalletBalance(newBalance);
        userRepository.save(user);

        WalletTransaction transaction = new WalletTransaction(user, amount, TransactionType.CREDIT);
        return walletTransactionRepository.save(transaction);
    }

    //  3. Deduct Funds (For Order Payments)
    @Transactional
    public WalletTransaction deductFunds(Long userId, double amount) {
        if (amount <= 0) throw new RuntimeException("Amount must be greater than zero");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getWalletBalance() < amount) {
            throw new RuntimeException("Insufficient balance in wallet");
        }

        user.setWalletBalance(user.getWalletBalance() - amount);
        userRepository.save(user);

        WalletTransaction transaction = new WalletTransaction(user, amount, TransactionType.DEBIT);
        return walletTransactionRepository.save(transaction);
    }

    // 4. Get Transaction History
    public List<WalletTransaction> getTransactionHistory(Long userId) {
        return walletTransactionRepository.findByUserId(userId);
    }
}
