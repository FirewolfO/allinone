package com.firewolf.lx.service;

import com.firewolf.lx.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: liuxing
 * Date: 2020/2/25 16:02
 * 用户业务操作
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;
}
