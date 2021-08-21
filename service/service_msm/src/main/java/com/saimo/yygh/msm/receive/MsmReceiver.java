package com.saimo.yygh.msm.receive;

import com.rabbitmq.client.Channel;
import com.saimo.yygh.msm.service.MsmService;
import com.saimo.yygh.rabbitmq.constant.MqConst;
import com.saimo.yygh.vo.msm.MsmVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author clearlove
 * @ClassName MsmReceiver.java
 * @Description
 * @createTime 2021年08月21日 22:52:00
 */
@Component
public class MsmReceiver {

    @Autowired
    private MsmService msmService;

    //监听
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_MSM_ITEM, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_MSM),
            key = {MqConst.ROUTING_MSM_ITEM}
    ))
    public void send(MsmVo msmVo, Message message, Channel channel) {
        msmService.send(msmVo);
    }
}
