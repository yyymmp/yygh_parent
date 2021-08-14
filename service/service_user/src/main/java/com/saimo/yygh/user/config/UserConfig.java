package com.saimo.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author clearlove
 * @ClassName UserConfig.java
 * @Description
 * @createTime 2021年08月12日 23:46:00
 */
@Configuration
@MapperScan("com.saimo.yygh.user.mapper")
public class UserConfig {

}
