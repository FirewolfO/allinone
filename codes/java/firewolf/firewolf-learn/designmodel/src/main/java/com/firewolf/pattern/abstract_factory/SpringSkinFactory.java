package com.firewolf.pattern.abstract_factory;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/14
 */
public class SpringSkinFactory implements SkinFactory {
    @Override
    public Button createButton() {
        return new SpringButton();
    }

    @Override
    public TextField createTextField() {
        return new SpringTextField();
    }

    @Override
    public ComboBox createComboBox() {
        return new SpringComboBox();
    }
}
