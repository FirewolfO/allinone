package com.firewolf.tools.email.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: liuxing
 * Date: 2020/3/10 11:50
 */
@Data
@ApiModel("邮件信息")
public class Email {
    @ApiModelProperty("接受方邮件")
    private String to;
    @ApiModelProperty("主题")
    private String title;
    @ApiModelProperty("内容")
    private String content;
    private MultipartFile[] attenchments;
}
