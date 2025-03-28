package com.example.restaurant_management.controller;

import com.example.restaurant_management.model.WalletTransaction;
import com.example.restaurant_management.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // 1. Get Wallet Balance
    @GetMapping("/{userId}/balance")
    public ResponseEntity<?> getWalletBalance(@PathVariable Long userId) {
        double balance = walletService.getWalletBalance(userId);
        return ResponseEntity.ok(Map.of("walletBalance", balance));
    }

    // 2. Add Funds to Wallet
    @PostMapping("/{userId}/add-funds")
    public ResponseEntity<?> addFunds(@PathVariable Long userId, @RequestBody Map<String, Double> request) {
        double amount = request.get("amount");
        WalletTransaction transaction = walletService.addFunds(userId, amount);
        return ResponseEntity.ok(Map.of("message", "Funds added successfully", "transaction", transaction));
    }

    // 3. Deduct Funds (For Order Payments)
    @PostMapping("/{userId}/deduct-funds")
    public ResponseEntity<?> deductFunds(@PathVariable Long userId, @RequestBody Map<String, Double> request) {
        double amount = request.get("amount");
        WalletTransaction transaction = walletService.deductFunds(userId, amount);
        return ResponseEntity.ok(Map.of("message", "Payment successful", "transaction", transaction));
    }

    // 4. Get Transaction History
    @GetMapping("/{userId}/transactions")
    public ResponseEntity<?> getTransactionHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getTransactionHistory(userId));
    }
}
