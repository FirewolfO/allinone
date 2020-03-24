package com.firewolf.example.springtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;


    @MockBean
    private UserMapper userMapper;


    @Test
    void addUser() {
        assertThrows(RuntimeException.class, () -> userService.addUser(User.builder().name("周扒皮").password("1111").build()), "account is too short");
        assertThrows(RuntimeException.class, () -> userService.addUser(User.builder().account("abc").name("周扒皮").password("1111").build()), "account is too short");

        String name = "myname";
        when(userMapper.selectCount(eq(User.builder().account(name).build()))).thenReturn(1);
        assertThrows(RuntimeException.class, () -> userService.addUser(User.builder().account(name).build()));
        assertDoesNotThrow(() -> userService.addUser(User.builder().account("wangba").build()));
    }

    @Test
    void selectUser() {
        when(userMapper.selectAll()).thenReturn(Arrays.asList(User.builder().account("zhangsan").build()));
        List<User> users = userService.selectUser();
        assertEquals(users.size(), 1);
    }


}