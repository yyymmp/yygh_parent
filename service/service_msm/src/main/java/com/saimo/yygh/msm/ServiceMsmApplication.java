package com.saimo.yygh.msm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author clearlove
 * @ClassName ServiceMsmApplication.java
 * @Description
 * @createTime 2021年08月14日 15:55:00
 */
//取消数据源自动配置
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
//扫描swagger
@ComponentScan(basePackages = {"com.saimo"})
public class ServiceMsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMsmApplication.class, args);
    }
}
