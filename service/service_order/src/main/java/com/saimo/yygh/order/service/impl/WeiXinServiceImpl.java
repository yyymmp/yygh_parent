package com.saimo.yygh.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.saimo.yygh.enums.PaymentTypeEnum;
import com.saimo.yygh.model.order.OrderInfo;
import com.saimo.yygh.order.service.OrderService;
import com.saimo.yygh.order.service.PaymentService;
import com.saimo.yygh.order.service.WeiXinService;
import com.saimo.yygh.order.utils.ConstantPropertiesUtils;
import com.saimo.yygh.order.utils.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author clearlove
 * @ClassName WeiXinServiceImpl.java
 * @Description
 * @createTime 2021年08月22日 12:59:00
 */
public class WeiXinServiceImpl implements WeiXinService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map createNative(Long orderId) {
        try {
            //从redis获取数据
            Map payMap = (Map)redisTemplate.opsForValue().get(orderId.toString());
            if(payMap != null) {
                return payMap;
            }
            //订单信息
            OrderInfo orderInfo = orderService.getById(orderId);
            //添加支付记录
            paymentService.savePaymentInfo(orderInfo, PaymentTypeEnum.WEIXIN);
            //设置参数 调用微信生成二维码接口
            //3设置参数，
            //把参数转换xml格式，使用商户key进行加密
            Map<String, String> paramMap = new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            String body = orderInfo.getReserveDate() + "就诊" + orderInfo.getDepname();
            paramMap.put("body", body);
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            //paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");
            paramMap.put("total_fee", "1"); //为了测试，统一写成这个值
            paramMap.put("spbill_create_ip", "127.0.0.1");
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            paramMap.put("trade_type", "NATIVE");
            //调用微信生成二维码接口,httpclient调用
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置map参数
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();

            //5 返回相关数据
            String xml = client.getContent();
            //转换map集合
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.println("resultMap:" + resultMap);
            //6 封装返回结果集
            Map map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("totalFee", orderInfo.getAmount());
            map.put("resultCode", resultMap.get("result_code"));
            map.put("codeUrl", resultMap.get("code_url")); //二维码地址

            if (resultMap.get("result_code") != null) {
                redisTemplate.opsForValue().set(orderId.toString(), map, 120, TimeUnit.MINUTES);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, String> queryPayStatus(Long orderId) {
        try {
            //1 根据orderId获取订单信息
            OrderInfo orderInfo = orderService.getById(orderId);

            //2 封装提交参数
            Map paramMap = new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

            //3 设置请求内容
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();

            //4 得到微信接口返回数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.println("支付状态resultMap:"+resultMap);
            //5 把接口数据返回
            return resultMap;
        }catch(Exception e) {
            return null;
        }
    }
}
