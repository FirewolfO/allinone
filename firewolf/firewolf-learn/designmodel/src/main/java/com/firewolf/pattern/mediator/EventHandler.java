package com.firewolf.pattern.mediator;

/**
 * 抽象同事
 */
public abstract class EventHandler {

    private EventBus eventBus;

    public EventHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * 处理事件
     * 消息对象
     */
    public abstract void handle(Object message);
}