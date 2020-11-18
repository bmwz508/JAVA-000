使用@Autowired进行属性的装配
使用set方法进行属性赋值
使用构造方法进行属性赋值
代码实现：

使用@Autowired进行属性的装配
package com.geek.week_05.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {
    @Autowired
    B b;
}
使用set方法进行属性赋值
package com.geek.week_05.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class B {
    C c;

    @Autowired
    public void setC(C c) {
        this.c = c;
    }
}
使用构造方法进行属性赋值
@Component
public class C {
    A a;

    public C(@Autowired A a) {
        this.a = a;
    }
}