/*
 * Decompiled with CFR.
 */
package com.firewolf.learn.test;

import com.firewolf.learn.test.User;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User findByName(String name) {
        return User.builder().age(new Random().nextInt(1001)).name(name).build();
    }
}

