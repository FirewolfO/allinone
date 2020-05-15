package com.firewolf.pattern.abstract_factory;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/15
 */
public class SummerSkinFactory implements SkinFactory {
    @Override
    public Button createButton() {
        return new SummerButton();
    }

    @Override
    public TextField createTextField() {
        return new SummerTextField();
    }

    @Override
    public ComboBox createComboBox() {
        return new SummerComboBox();
    }
}
