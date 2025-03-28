package com.example.restaurant_management.repository;

import com.example.restaurant_management.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findByUserId(Long userId);
    List<WalletTransaction> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
