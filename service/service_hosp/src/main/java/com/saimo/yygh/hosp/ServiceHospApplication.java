package com.saimo.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author clearlove
 * @ClassName ServiceHospApplication.java
 * @Description
 * @createTime 2021年07月26日 23:12:00
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.saimo")
//注册服务
@EnableDiscoveryClient
//找到service_cmn_client模块
@EnableFeignClients(basePackages = "com.saimo")
public class ServiceHospApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
