package com.firewolf.pattern.abstract_factory;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/14
 */
public interface SkinFactory {

    Button createButton();

    TextField createTextField();

    ComboBox createComboBox();
}
