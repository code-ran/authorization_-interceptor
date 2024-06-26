package com.ransibi.controller;

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
@RequestMapping("user")
public class ErrorController {
    @GetMapping({"/error"})
    public String getLoginPage() {
        return "认证失败，无法访问系统资源!";
    }
}
