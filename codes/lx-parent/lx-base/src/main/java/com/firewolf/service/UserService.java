package com.firewolf.service;

import com.firewolf.dto.UserDto;
import com.firewolf.mapper.UserMapper;
import com.firewolf.vo.UserVO;
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

    public void add(UserVO user){
        userMapper.insertSelective(UserDto.tansVo2PO(user));
    }
}
