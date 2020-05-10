package com.firewolf.test.desinpattern.prototype;

import lombok.Data;

import java.io.*;

/**
 * 描述：Person类
 * Author：liuxing
 * Date：2020/5/9
 */
@Data
public class Person implements Serializable, Cloneable {

    private String name;

    private Son son;

    public Person clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Person result = (Person) clone;
        return result;
    }

    public Person deepClone1() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Person result = (Person) clone;
        result.setSon(result.getSon().clone()); // 拷贝子对象
        return result;
    }

    public Person deepClone() {
        try {
            //将对象写入到流中
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(this);

            // 从流中读出对象
            ByteArrayInputStream bai = new ByteArrayInputStream(bao.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bai);
            return (Person) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
