package com.saimo.yygh.msm.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author clearlove
 * @ClassName ConstantPropertiesUtils.java
 * @Description
 * @createTime 2021年08月14日 15:57:00
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    @Value("${aliyun.sms.regionId}")
    private String regionId;

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.secret}")
    private String secret;

    public static String REGION_Id;
    public static String ACCESS_KEY_ID;
    public static String SECRECT;

    //在读取之后执行
    @Override
    public void afterPropertiesSet() throws Exception {
        REGION_Id = regionId;
        ACCESS_KEY_ID = accessKeyId;
        SECRECT = secret;
    }
}
