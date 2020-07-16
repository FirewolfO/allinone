package com.firewolf.pattern.observer;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/16
 */
public class Client {
    public static void main(String[] args) {
        Button button = new Button("绿色",20);

        // 添加监听器
        button.addListener(new ChangeColorListener());
        ChangeSizeListener sizeListener = new ChangeSizeListener();
        button.addListener(sizeListener);

        System.out.println(button);

        button.click();
        System.out.println(button);

        // 移除大小改变监听器
        button.removeListener(sizeListener);
        button.click();
        System.out.println(button);
    }
}
