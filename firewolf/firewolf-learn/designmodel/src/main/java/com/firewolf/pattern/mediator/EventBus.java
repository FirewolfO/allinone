package com.firewolf.pattern.mediator;

/**
 * 事件总线，充当抽象调停者
 */
public interface EventBus {
    /**
     * 发布事件
     * @param type    事件类型
     * @param message 消息对象
     */
    void post(String type, Object message);
}