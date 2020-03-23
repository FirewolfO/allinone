package com.firewolf.busi.example.springtest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public void addUser(User user) {
        if (StringUtils.isEmpty(user.getAccount()) || user.getAccount().length() < 4) {
            throw new RuntimeException("account is too short");
        }
        int i = userMapper.selectCount(User.builder().account(user.getAccount()).build());
        if (i > 0) {
            throw new RuntimeException("account can not repeat");
        }
        userMapper.insertSelective(user);
    }

    public List<User> selectUser() {
        return userMapper.selectAll();
    }
}
