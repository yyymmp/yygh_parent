package com.saimo.yygh.cmn.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author clearlove
 * @ClassName HospConfig.java
 * @Description
 * @createTime 2021年07月27日 23:34:00
 */
@Configuration
@MapperScan("com.saimo.yygh.cmn.mapper")
public class CmnConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor();
    }
}
