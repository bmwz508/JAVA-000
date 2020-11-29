package com.example.dynamic.entity;

import lombok.Data;


/**
 * @author linmf
 * @Description
 * @date 2020/11/29 0:19
 */
@Data
public class User {

    Long id;

    /**
     * 名称
     */
    String name;

    /**
     * 密码
     */
    String password;
}
