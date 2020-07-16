package com.firewolf.pattern.mediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultEventBus implements EventBus {
    private static DefaultEventBus instance;
    private static Object lockObj = new Object();
    private Map<String, List<EventHandler>> eventHandlerMap;

    private DefaultEventBus() {
        eventHandlerMap = new HashMap<>();
    }

    public static DefaultEventBus getInstance() {
        if (null == instance) {
            synchronized (lockObj) {
                if (null == instance) {
                    instance = new DefaultEventBus();
                }
            }
        }
        return instance;
    }

    @Override
    public void post(String type, Object message) {
        List<EventHandler> handlers = eventHandlerMap.get(type);
        if (null != handlers) {
            for (EventHandler handler : handlers) {
                handler.handle(message);
            }
        }
    }

    /**
     * 注册事件处理器
     */
    public void registerHandler(String type, EventHandler eventHandler) {
        if (eventHandlerMap.containsKey(type)) {
            List handlers = eventHandlerMap.get(type);
            handlers.add(eventHandler);
        } else {
            List handlers = new ArrayList<>();
            handlers.add(eventHandler);
            eventHandlerMap.put(type, handlers);
        }
    }
}
