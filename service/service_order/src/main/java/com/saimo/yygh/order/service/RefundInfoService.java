package com.saimo.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saimo.yygh.model.order.PaymentInfo;
import com.saimo.yygh.model.order.RefundInfo;

/**
 * @author clearlove
 * @ClassName RefundInfoService.java
 * @Description
 * @createTime 2021年08月22日 21:26:00
 */
public interface RefundInfoService extends IService<RefundInfo> {

    public RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}
