package com.example.dynamic.service;

import com.example.dynamic.entity.User;
import com.example.dynamic.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author linmf
 * @Description
 * @date 2020/11/29 21:28
 */
@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public List<User> findAll(){
        return userMapper.findAll();
    }

    @Transactional
    public int insert(User user){
        return userMapper.insert(user);
    }

    @Transactional
    public int update(User user){
        int update = userMapper.update(user);
        System.out.println(userMapper.findAll());
        return update;
    }

}
