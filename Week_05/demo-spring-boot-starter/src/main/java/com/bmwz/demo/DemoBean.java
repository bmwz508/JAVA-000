package com.bmwz.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sun.security.ssl.Debug;

/**
 * @author linmf
 * @Description
 * @date 2020/11/18 16:43
 */
public class DemoBean {

    @Value("${spring.demo.level:debug}")
    private String level;

    public void test(){
        System.out.println("level值为：" + level);
    }

}
