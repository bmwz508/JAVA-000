package com.example.dynamic.mapper;

import com.example.dynamic.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author linmf
 * @Description
 * @date 2020/11/29 21:28
 */
@Mapper
public interface UserMapper {

    List<User> findAll();

    int insert(User user);

    int update(User user);

}
