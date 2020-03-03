package com.firewolf.lx.web;

/**
 * Author: liuxing
 * Date: 2020/2/24 20:45
 */

import com.firewolf.lx.domain.Response;
import com.firewolf.lx.domain.ResponseEnum;
import com.firewolf.lx.service.UserService;
import com.firewolf.lx.tools.log.LXLog;
import com.firewolf.lx.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户处理器
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户操作")
public class UserController extends BaseController {


    @Autowired
    private UserService userService;


    @GetMapping
    @LXLog(operate = "test log", start = "start test user")
    public Response<String> test() {
        return Response.ok("hello");
    }

    @PostMapping
    @ApiOperation("添加用户")
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功"),
            @ApiResponse(code = 500,message = "插入失败")
    })
    public Response<String> addUser(@RequestBody UserVO userVO) {
        userService.add(userVO);
        return Response.ok(ResponseEnum.OK.getMsg());
    }
}
