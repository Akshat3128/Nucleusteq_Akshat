package com.example.demo.repository;

import com.example.demo.model.Hr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HrRepository extends JpaRepository<Hr, Long> {
    Optional<Hr> findByEmailAndPassword(String email, String password);
}
