package com.zhifou.note.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author : li
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

    @ApiOperation("跳转到首页")
    @RequestMapping("/toIndex")
    public String index(){
        return "redirect:/";
    }

    @ApiOperation("首页")
    @RequestMapping("/")
    public String toIndex(){
        return "index";
    }
}
