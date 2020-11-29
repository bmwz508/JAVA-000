package com.example.dynamic;

import com.example.dynamic.entity.User;
import com.example.dynamic.mapper.UserMapper;
import com.example.dynamic.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DynamicDataSourceApplicationTests {

    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
        User user = new User();
        user.setId(1L);
        user.setName("li");
        user.setPassword("123");
//        userService.insert(user);
        userService.update(user);
//        System.out.println(userService.findAll());
    }

}
