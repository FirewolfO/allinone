package com.firewolf.tools.email.web;

import com.firewolf.domain.ResponseEnum;
import com.firewolf.domain.Response;
import com.firewolf.tools.email.entity.Email;
import com.firewolf.tools.email.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: liuxing
 * Date: 2020/3/10 11:51
 */
@RestController
@RequestMapping("/email")
@Api(tags = "邮件操作")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    @ApiOperation("发送邮件")
    public Response<String> sendEmail(Email email) {
        emailService.sendSimpleMail(email.getTo(), email.getTitle(), email.getContent());
        return Response.ok(ResponseEnum.OK.getMsg());
    }

}
