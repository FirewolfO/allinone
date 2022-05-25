package com.firewolf.pattern.observer;

/**
 * 描述：改变大小的观察者，把大小一直+10
 * Author：liuxing
 * Date：2020/7/16
 */
public class ChangeSizeListener implements ClickListener {
    @Override
    public void clicked(Clickable clickable) {
        if (clickable instanceof Button) {
            Button button = (Button) clickable;
            button.setSize(button.getSize() + 10);
        }
    }
}
