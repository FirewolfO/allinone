package com.firewolf.simple;

import io.netty.util.CharsetUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import static com.firewolf.constant.MQConstant.*;
import static com.firewolf.util.Logger.*;

/**
 * 描述：发送同步消息的生产者
 * Author：liuxing
 * Date：2020/5/12
 */
public class SyncProducer {

    public static void main(String[] args) throws Exception {

        // 创建生产者
        DefaultMQProducer mqProducer = new DefaultMQProducer();
        mqProducer.setSendMsgTimeout(10000);

        // 设置NameServer
        mqProducer.setNamesrvAddr(nameServer);

        // 设置生产者组
        mqProducer.setProducerGroup(produceGroup);

        // 启动生产者
        mqProducer.setVipChannelEnabled(false);
        mqProducer.start();

        // 发送100条消息
        for (int i = 0; i < 100; i++) {
            Message message = new Message(topic,"tagA" ,("Hello" + i).getBytes(CharsetUtil.UTF_8));
            SendResult sendResult = mqProducer.send(message);
            console("message {} result = {} ", i, sendResult);
        }
        // 关闭生产者
        mqProducer.shutdown();
    }
}
