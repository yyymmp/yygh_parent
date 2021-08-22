package com.saimo.yygh.order.api;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.order.service.PaymentService;
import com.saimo.yygh.order.service.WeiXinService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName WeixinController.java
 * @Description
 * @createTime 2021年08月22日 12:58:00
 */

@RestController
@RequestMapping("/api/order/weixin")
public class WeixinController {

    @Autowired
    private WeiXinService weiXinService;

    @Autowired
    private PaymentService paymentService;

    //生成微信支付二维码
    @GetMapping("createNative/{orderId}")
    public Result createNative(@PathVariable Long orderId) {
        Map map = weiXinService.createNative(orderId);
        return Result.ok(map);
    }

    //查询支付状态  通过订单id查询
    @GetMapping("queryPayStatus/{orderId}")
    public Result queryPayStatus(@PathVariable Long orderId) {
        //调用微信接口实现支付状态查询
        Map<String, String> resultMap = weiXinService.queryPayStatus(orderId);
        //判断
        if (resultMap == null) {
            return Result.fail().message("支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) { //支付成功
            //更新订单状态
            String out_trade_no = resultMap.get("out_trade_no");//订单编码
            paymentService.paySuccess(out_trade_no, resultMap);
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中");
    }

}
