package com.saimo.yygh.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.saimo.yygh.msm.service.MsmService;
import com.saimo.yygh.msm.utils.ConstantPropertiesUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author clearlove
 * @ClassName MsmServiceImpl.java
 * @Description
 * @createTime 2021年08月14日 16:02:00
 */
@Slf4j
@Service
public class MsmServiceImpl implements MsmService {

    @Override
    public boolean send(String phone, String code) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        //整合阿里云短信服务
        //设置相关参数
        DefaultProfile profile = DefaultProfile.
                getProfile(ConstantPropertiesUtils.REGION_Id,
                        ConstantPropertiesUtils.ACCESS_KEY_ID,
                        ConstantPropertiesUtils.SECRECT);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //手机号
        request.putQueryParameter("PhoneNumbers", phone);
        //签名名称
        request.putQueryParameter("SignName", "我的谷粒在线教育网站");
        //模板code
        request.putQueryParameter("TemplateCode", "SMS_180051135");
        //验证码  使用json格式   {"code":"123456"}
        Map<String, Object> param = new HashMap();
        param.put("code", code);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
        //进行发送
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            log.error(e.getMessage());
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
