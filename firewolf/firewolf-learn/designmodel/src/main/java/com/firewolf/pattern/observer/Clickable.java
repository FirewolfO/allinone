package com.firewolf.pattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：抽象目标
 * Author：liuxing
 * Date：2020/7/16
 */
public abstract class Clickable {

    /**
     * 观察者列表
     */
    List<ClickListener> listeners = new ArrayList<>();

    /**
     * 添加观察者
     *
     * @param listener
     */
    public void addListener(ClickListener listener) {
        listeners.add(listener);
    }

    /**
     * 删除观察者
     *
     * @param listener
     */
    public void removeListener(ClickListener listener) {
        listeners.remove(listener);
    }

    /**
     * 通知观察者
     */
    public void notifyListeners() {
        listeners.forEach(listener -> listener.clicked(this));
    }

}
