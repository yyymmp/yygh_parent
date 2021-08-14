package com.saimo.yygh.user.api;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.user.utils.ConstantWxPropertiesUtils;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author clearlove
 * @ClassName WeixinApiController.java
 * @Description
 * @createTime 2021年08月14日 18:19:00
 */
//微信操作的接口
@Controller //为了跳转方便
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    //1 生成微信扫描二维码
    //返回生成二维码需要参数  前端根据参数生成二维码
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", ConstantWxPropertiesUtils.WX_OPEN_APP_ID);
            map.put("scope", "snsapi_login");
            String wxOpenRedirectUrl = ConstantWxPropertiesUtils.WX_OPEN_REDIRECT_URL;
            wxOpenRedirectUrl = URLEncoder.encode(wxOpenRedirectUrl, "utf-8");
            map.put("redirect_uri", wxOpenRedirectUrl);
            map.put("state", System.currentTimeMillis() + "");
            return Result.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
