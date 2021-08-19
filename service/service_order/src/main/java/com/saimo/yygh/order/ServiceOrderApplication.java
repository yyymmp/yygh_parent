package com.saimo.yygh.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author clearlove
 * @ClassName ServiceOrderApplication.java
 * @Description
 * @createTime 2021年08月19日 23:43:00
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.saimo"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.saimo"})
public class ServiceOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}
