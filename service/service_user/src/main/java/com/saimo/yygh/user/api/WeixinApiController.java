package com.saimo.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.saimo.yygh.common.helper.JwtHelper;
import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.model.user.UserInfo;
import com.saimo.yygh.user.service.UserInfoService;
import com.saimo.yygh.user.utils.ConstantWxPropertiesUtils;
import com.saimo.yygh.user.utils.HttpClientUtils;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
@Slf4j
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;

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

    //获取扫描人的信息
    //前端微信用户点击确认后,微信会自动回调一个地址 就是在微信后台配置的地址  但该地址需要是一个线上域名地址(实际开发中只需要配置线上地址即可)
    //微信扫码后回调方法
    @GetMapping("callback")
    public String callback(String code, String state) {
        //根据code请求微信地址 得到accesstoken 和 openid
        log.info("code: {}", code);
        //  %s   占位符
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");
        //%s占位符  String.format依次填充该占位符
        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantWxPropertiesUtils.WX_OPEN_APP_ID,
                ConstantWxPropertiesUtils.WX_OPEN_APP_SECRET,
                code);
        //根据accesstoken 和 openid 请求微信地址 返回扫码人的信息 昵称等

        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //从返回中获取accesstoken 和 openid
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            String access_token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");

            UserInfo userInfo = userInfoService.selectWxInfoOpenId(openid);
            if (userInfo == null) {
                //根据accesstoken 和 openid 请求微信地址 返回扫码人的信息 昵称等
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String resultInfo = HttpClientUtils.get(userInfoUrl);

                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
                //解析用户信息
                //用户昵称
                String nickname = resultUserInfoJson.getString("nickname");
                //用户头像
                String headimgurl = resultUserInfoJson.getString("headimgurl");

                //获取扫描人信息添加数据库
                userInfo = new UserInfo();
                userInfo.setNickName(nickname);
                userInfo.setOpenid(openid);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }
            //返回name token
            Map<String, String> map = new HashMap<>();
            String name = userInfo.getName();
            if (StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if (StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);

            //判断userInfo是否有手机号，如果手机号为空，返回openid
            //如果手机号不为空，返回openid值是空字符串
            //前端判断：如果openid不为空，绑定手机号，如果openid为空，不需要绑定手机号
            if (StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }

            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            //跳转到前端页面
            return "redirect:" + ConstantWxPropertiesUtils.YYGH_BASE_URL + "/weixin/callback?token=" + map.get("token") + "&openid=" + map.get("openid")
                    + "&name=" + URLEncoder.encode(map.get("name"), "utf-8");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        //将扫码人的信息存入数据库
        return null;
    }

}
