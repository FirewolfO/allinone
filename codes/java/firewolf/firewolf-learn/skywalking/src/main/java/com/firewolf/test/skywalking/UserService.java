package com.firewolf.test.skywalking;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/8
 */
@Service
public class UserService {


    public User findByName(String name) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<User> all = findAll();
        Optional<User> first = all.stream().filter(x -> x.getName().equals(name)).findFirst();
        return first.orElse(null);
    }

    private List<User> findAll() {
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(
                new User("zhangsan", 12),
                new User("lisi", 50),
                new User("wangwu", 90)
        );
    }
}
