package com.saimo.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saimo.yygh.enums.PaymentTypeEnum;
import com.saimo.yygh.model.order.OrderInfo;
import com.saimo.yygh.model.order.PaymentInfo;
import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {

    void savePaymentInfo(OrderInfo orderInfo, PaymentTypeEnum weixin);

    void paySuccess(String out_trade_no, Map<String, String> resultMap);
}
