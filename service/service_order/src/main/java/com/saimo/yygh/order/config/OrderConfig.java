package com.saimo.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author clearlove
 * @ClassName HospConfig.java
 * @Description
 * @createTime 2021年07月27日 23:34:00
 */
@Configuration
@MapperScan("com.saimo.yygh.order.mapper")
public class OrderConfig {

}
