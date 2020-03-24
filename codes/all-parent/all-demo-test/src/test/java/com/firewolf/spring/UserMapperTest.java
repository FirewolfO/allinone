package com.firewolf.spring;

import com.firewolf.entity.User;
import com.firewolf.mybatis.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
        // 如果想在数据库看到更新的数据，这里需要设置去掉事务，
    void addUser() {
        int count1 = userMapper.selectCount(User.builder().account("mmmmm").build());
        userMapper.insertSelective(User.builder().account("mmmmm").build());
        int count2 = userMapper.selectCount(User.builder().account("mmmmm").build());
        Assertions.assertEquals(count1 + 1, count2);
    }

    @Test
    void selectUsers() {
        Assertions.assertDoesNotThrow(() -> userMapper.selectAll());
    }
}

