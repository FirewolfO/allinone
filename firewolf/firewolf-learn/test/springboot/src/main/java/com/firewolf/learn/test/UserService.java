package com.firewolf.learn.test;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/27
 */
@Service
public class UserService {

    public User findByName(String name) {
        return User.builder()
                .age(new Random().nextInt(100))
                .name(name)
                .build();
    }
}
