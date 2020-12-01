package com.example.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

import java.sql.SQLException;

@MapperScan(basePackages = "com.example.sharding.repository")
@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class ShardingJdbcApplication {

    public static void main(final String[] args) throws SQLException {
            SpringApplication.run(ShardingJdbcApplication.class, args);
    }
}
