package com.firewolf.spring;

import com.firewolf.entity.User;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/users")
@RestController
@Api(tags = "用户操作接口")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping
    public String addUser(User user) {
        if (StringUtils.isEmpty(user.getAccount())) {
            throw new RuntimeException("account can not be empty");
        }
        userService.addUser(user);
        return "success";
    }

    @GetMapping
    public List<User> selectUser() {
        return userService.selectUser();
    }
}
