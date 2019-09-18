package com.huamai.hdServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auther: byj
 * @Date: 2019/3/14 15:12
 * @Description:  异常处理控制器
 */
@Controller
public class ErrorController {
    @GetMapping("/error")
    public String error(){
        return "error";
    }
}
