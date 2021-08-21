package com.saimo.yygh.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author clearlove
 * @ClassName RabbitService.java
 * @Description
 * @createTime 2021年08月21日 22:18:00
 */
@Service
public class RabbitService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    //发送消息
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }
}
