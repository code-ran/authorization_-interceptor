package com.ransibi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: rsb
 * @description: 2023-11-15-10-56
 * @description:  示例接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("rsb")
public class IntercetorController {
    @GetMapping({"/auth/test"})
    public String getHeaderInfo() {
        System.out.println("getHeaderInfo");
        return "getHeaderInfo";
    }

    @GetMapping({"/auth/test/list"})
    public String getHeaderInfoLst() {
        System.out.println("getHeaderInfoLst");
        return "getHeaderInfoLst";
    }
}
