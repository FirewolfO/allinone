package com.firewolf.pattern.observer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Button extends Clickable {

    /**
     * 目标的一些状态信息
     */
    private String color;
    private int size;


    /**
     * 业务方法
     */
    public void click() {
        System.out.println("点击了按钮...");
        this.notifyListeners();
    }
}
