package com.example.demo.service;

import com.example.demo.model.Hr;
import com.example.demo.repository.HrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import java.util.Optional;

@Service
public class HrService {

    @Autowired
    private HrRepository hrRepository;

    public boolean authenticate(String email, String password) {
        return hrRepository.findByEmailAndPassword(email, password).isPresent();
    }

    public void registerHr(String email, String password) {
        hrRepository.save(new Hr(email, password));
    }
}
