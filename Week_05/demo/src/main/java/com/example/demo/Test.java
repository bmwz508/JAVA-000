package com.example.demo;

import com.bmwz.demo.DemoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author linmf
 * @Description
 * @date 2020/11/18 16:52
 */
@Component
public class Test implements CommandLineRunner {

    @Autowired
    DemoBean demoBean;

    @Override
    public void run(String... args) throws Exception {
        demoBean.test();
    }
}
