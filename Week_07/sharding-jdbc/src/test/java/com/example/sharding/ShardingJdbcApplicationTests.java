package com.example.sharding;

import com.example.sharding.service.ExampleService;
import com.example.sharding.service.OrderServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingJdbcApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ShardingJdbcApplicationTests {

    @Autowired
    ExampleService exampleService;

    @Test
    public void contextLoads() throws SQLException {
        try {
//            exampleService.initEnvironment();
            exampleService.processSuccess();
//            exampleService.printData();
        } finally {
//            exampleService.cleanEnvironment();
        }
    }

}
