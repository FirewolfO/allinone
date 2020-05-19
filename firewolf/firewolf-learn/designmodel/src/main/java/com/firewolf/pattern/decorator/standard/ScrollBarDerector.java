package com.firewolf.pattern.decorator.standard;

/**
 * Description: 具体装饰类
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/19 11:34 下午
 */
public class ScrollBarDerector extends ComponentDeretor {

    public ScrollBarDerector(Component component) {
        super(component);
    }

    @Override
    public void display() {
        super.display();
        this.setScrollBar();
    }

    public void setScrollBar() {
        System.out.println("设置滚动条");
    }

}
