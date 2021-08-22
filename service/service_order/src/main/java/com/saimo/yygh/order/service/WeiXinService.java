package com.saimo.yygh.order.service;

import java.util.Map;

public interface WeiXinService {

    Map createNative(Long orderId);

    Map<String, String> queryPayStatus(Long orderId);
}
