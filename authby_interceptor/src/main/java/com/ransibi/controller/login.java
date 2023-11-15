package com.ransibi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: rsb
 * @description: 2023-11-15-14-04
 * @description:
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("login")
public class login {
    @GetMapping({"/toLoginPage"})
    public String getLoginPage() {
        return "认证失败，无法访问系统资源!";
    }
}
