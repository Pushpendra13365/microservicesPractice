package com.auth_service.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionConfig {
    public HikariDataSource hikariDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/microservices");
        config.setUsername("root");
        config.setPassword("Pushpendra#345");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver.");
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(6000000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);
        return new HikariDataSource(config);
    }
}
