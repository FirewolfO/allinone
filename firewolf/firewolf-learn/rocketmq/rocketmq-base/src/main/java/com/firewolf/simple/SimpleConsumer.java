package com.firewolf.simple;

import io.netty.util.CharsetUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

import static com.firewolf.constant.MQConstant.*;
import static com.firewolf.util.Logger.*;

/**
 * 描述：简单消息消费者
 * Author：liuxing
 * Date：2020/5/12
 */
public class SimpleConsumer {

    public static void main(String[] args) throws Exception {


        // 创建消费者
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer(consumerGroup);

        // 设置nameServer
        mqPushConsumer.setNamesrvAddr(nameServer);

        // 订阅消息，topic 和 tag
        mqPushConsumer.subscribe(topic, "*");

        // 注册监听，用于接收到消息之后回调
        mqPushConsumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {

            list.stream().forEach(messageExt ->
                    // 处理消息
                    console(new String(messageExt.getBody(), CharsetUtil.UTF_8))
            );

            // 返回消费结果
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        Thread.sleep(Long.MAX_VALUE);
    }
}
