package com.saimo.yygh.rabbitmq.constant;

/**
 * @author clearlove
 * @ClassName MqConst.java
 * @Description
 * @createTime 2021年08月21日 22:19:00
 */
public class MqConst {

    //****************预约下单
    public static final String EXCHANGE_DIRECT_ORDER = "exchange.direct.order";
    public static final String ROUTING_ORDER = "order";
    //队列
    public static final String QUEUE_ORDER = "queue.order";


    //***************短信
    //短信交换机
    public static final String EXCHANGE_DIRECT_MSM = "exchange.direct.msm";
    //路由键
    public static final String ROUTING_MSM_ITEM = "msm.item";
    //队列名
    public static final String QUEUE_MSM_ITEM = "queue.msm.item";


    public static final String QUEUE_TASK_8 = "queue.task.8";
    public static final String EXCHANGE_DIRECT_TASK = "exchange.direct.task";
    public static final String ROUTING_TASK_8 = "task.8";
}
