package com.saimo.yygh.order.api;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.order.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName OrderApiController.java
 * @Description
 * @createTime 2021年08月19日 23:49:00
 */
@RestController
@RequestMapping("admin/hosp/department")
@Api(tags = "订单管理")
public class OrderApiController {

    @Autowired
    private OrderService orderService;


    //生成挂号订单
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result savaOrders(@PathVariable String scheduleId,
            @PathVariable Long patientId) {
        Long orderId = orderService.saveOrder(scheduleId,patientId);
        return Result.ok(orderId);
    }
}
