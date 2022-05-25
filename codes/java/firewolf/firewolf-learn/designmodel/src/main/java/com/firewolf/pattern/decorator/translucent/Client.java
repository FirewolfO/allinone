package com.firewolf.pattern.decorator.translucent;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/19 11:46 下午
 */
public class Client {

    public static void main(String[] args) {

        Component textField = new TextField();
        textField.display();
        System.out.println("-----------");
        ScrollBarDerector scrollTxtField = new ScrollBarDerector(textField);
        scrollTxtField.setScrollBar();

        System.out.println("-----------");
        YellowBorderDerector scrollBorderdTextField = new YellowBorderDerector(scrollTxtField);

        scrollBorderdTextField.setBorder();

    }
}
