package com.firewolf.lx.tools.email.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: liuxing
 * Date: 2020/3/10 11:50
 */
@Data
public class Email {

    private String from;
    private String fromPassword;
    private String to;
    private String title;
    private String content;
    private MultipartFile[] attenchments;
}
