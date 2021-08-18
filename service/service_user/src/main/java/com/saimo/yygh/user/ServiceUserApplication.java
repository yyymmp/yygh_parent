package com.saimo.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author clearlove
 * @ClassName ServiceUserApplication.java
 * @Description
 * @createTime 2021年08月12日 23:34:00
 */
@SpringBootApplication
//注册到nacos
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.saimo")
@EnableFeignClients(basePackages = "com.saimo")
public class ServiceUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
