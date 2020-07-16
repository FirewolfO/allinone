package com.firewolf.pattern.observer;

import java.util.Arrays;
import java.util.List;

/**
 * 描述：循环切换颜色，具体观察者
 * Author：liuxing
 * Date：2020/7/16
 */
public class ChangeColorListener implements ClickListener {

    private List<String> colors = Arrays.asList("红色", "黄色", "绿色", "紫色");

    @Override
    public void clicked(Clickable clickable) {
        if (clickable instanceof Button) {
            Button button = (Button) clickable;
            String color = button.getColor();
            int i = colors.indexOf(color);
            button.setColor(colors.get((i + 1) % colors.size()));
        }
    }
}
