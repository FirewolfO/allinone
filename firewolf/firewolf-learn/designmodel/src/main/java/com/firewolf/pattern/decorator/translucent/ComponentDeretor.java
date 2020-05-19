package com.firewolf.pattern.decorator.translucent;

/**
 * Description: 抽象装饰类
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/19 11:31 下午
 */
public class ComponentDeretor extends Component {

    private Component component;

    public ComponentDeretor(Component component) {
        this.component = component;
    }

    @Override
    public  void display() {
        component.display(); // 只是简单的调用构件的原始方法
    }
}
