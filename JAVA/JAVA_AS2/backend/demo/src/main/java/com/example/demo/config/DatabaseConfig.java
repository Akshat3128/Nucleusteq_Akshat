package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    @Bean
    public DataSource dataSource() {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(dbUrl);
            dataSource.setUsername(dbUsername);
            dataSource.setPassword(dbPassword);
            dataSource.setDriverClassName(dbDriver);

            logger.info("Database connected successfully!");
            return dataSource;
        } catch (Exception e) {
            logger.error(" Database connection failed: ", e);
            throw new RuntimeException("Database connection error", e);
        }
    }
}
