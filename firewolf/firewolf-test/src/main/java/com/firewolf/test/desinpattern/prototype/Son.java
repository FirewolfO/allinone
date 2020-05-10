package com.firewolf.test.desinpattern.prototype;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/9
 */
@Data
public class Son implements Serializable, Cloneable {

    private String name;

    private Dog dog;

    @Override
    public Son clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return (Son) clone;
    }
}
