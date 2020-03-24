package com.firewolf.busi.example.springtest;

import org.junit.jupiter.api.Test;
//import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void addUser() {
        userMapper.insertSelective(User.builder().account("mmmmm").build());

        int mmmmm = userMapper.selectCount(User.builder().account("mmmmm").build());
        System.out.println(mmmmm);

    }
}

