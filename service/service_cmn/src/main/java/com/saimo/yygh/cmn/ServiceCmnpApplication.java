package com.saimo.yygh.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author clearlove
 * @ClassName ServiceHospApplication.java
 * @Description
 * @createTime 2021年07月26日 23:12:00
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.saimo")
//注册到nacos
@EnableDiscoveryClient
public class ServiceCmnpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnpApplication.class, args);
    }
}
