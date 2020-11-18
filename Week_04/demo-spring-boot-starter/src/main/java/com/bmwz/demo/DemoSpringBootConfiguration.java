package com.bmwz.demo;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.demo", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DemoSpringBootConfiguration {

    @Bean
    public DemoBean demoBeanConfiguration(){
        return new DemoBean();
    }

    @Bean
    public Student studentConfiguration(){
        return new Student();
    }


    @Bean
    public Klass klassConfiguration(){
        return new Klass();
    }

    @Bean
    public School schoolConfiguration(){
        return new School();
    }
    
}

