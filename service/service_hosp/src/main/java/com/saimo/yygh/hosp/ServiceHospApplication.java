package com.saimo.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author clearlove
 * @ClassName ServiceHospApplication.java
 * @Description
 * @createTime 2021年07月26日 23:12:00
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.saimo")
public class ServiceHospApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
