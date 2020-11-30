package com.example.dynamic.config;

import com.example.dynamic.datasource.DynamicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linmf
 * @Description
 * @date 2020/11/29 0:14
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource master() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave1")
    public DataSource slave1() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put("master", master());
        targetDataSources.put("slave1", slave1());
        return new DynamicDataSource(master(), targetDataSources);
    }

}
