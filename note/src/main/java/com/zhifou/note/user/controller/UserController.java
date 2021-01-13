package com.zhifou.note.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : li
 * @Date: 2021-01-05 15:05
 */
@Api("登录")
@Controller
public class UserController {

    @ApiOperation("登录")
    @RequestMapping("/login")
    public String toLogin(){
        return "login";
    }

    @ApiOperation("首页")
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
