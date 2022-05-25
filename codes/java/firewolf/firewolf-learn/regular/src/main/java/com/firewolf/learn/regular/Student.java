package com.firewolf.learn.regular;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/2 3:31 下午
 */
public class Student {
    private String name;
    private Integer age;

    private String sayHello(String name) {
        return "Hello,I'm " + name;
    }

    private void disPlay() {
        System.out.println("name=" + name + ",age=" + age);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public static void main(String[] args) throws Exception {
        Student s = new Student();
        Method method = Student.class.getDeclaredMethod("disPlay");
        method.invoke(s);
    }
}
