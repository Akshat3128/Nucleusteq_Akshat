package com.example.restaurant_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class RestaurantManagementApplication {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantManagementApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(RestaurantManagementApplication.class, args);
            logger.info(" Server started successfully on port 8080!");
            logger.info(" Database connected successfully!");
        } catch (Exception e) {
            logger.error(" Error starting server: ", e);
        }
    }
}
