package com.firewolf.busi.example.springtest;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@SpringBootTest
@MybatisTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE )
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class UserControllerTest {

    @SpyBean
    private UserService userService;

    @Test
    void addUser() throws Exception {
      userService.addUser(User.builder().account("mmmmm").build());
    }

    @Test
    void selectUser() throws Exception {
        List<User> users =  userService.selectUser();

        System.out.println(users.size());
    }
}