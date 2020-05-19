package com.firewolf.pattern.decorator.standard;

/**
 * Description: 具体的装饰类
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/19 11:38 下午
 */
public class YellowBorderDerector extends ComponentDeretor {

    public YellowBorderDerector(Component component) {
        super(component);
    }

    @Override
    public void display() {
        super.display();
        this.setBorder();
    }

    public void setBorder() {
        System.out.println("设置黄色的边框");
    }
}
