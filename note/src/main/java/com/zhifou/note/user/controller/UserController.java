package com.zhifou.note.user.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.zhifou.note.util.RedisKeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author : li
 * @Date: 2021-01-05 15:05
 */
@Api("用户")
@Controller
public class UserController {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @ResponseBody
    @RequestMapping("/userInfo")
    public Object userInfo(){
        Object user = redisTemplate.opsForValue().get("user");
        Set<Object> ids = redisTemplate.opsForSet().members("ids");
        return user;
    }

    @ApiOperation("获取图片验证码")
    @RequestMapping(value = "/getImageCode",method = RequestMethod.GET)
    public void getCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 40,4,8);
        captcha.setBackground(Color.LIGHT_GRAY);
//        System.out.println(captcha.getCode());
        redisTemplate.opsForValue().set(RedisKeyUtil.getImageCodeKey(request.getRemoteAddr()),captcha.getCode(),5, TimeUnit.MINUTES);
//        request.getSession().setAttribute("code",captcha.getCode());
        ServletOutputStream out = response.getOutputStream();
        captcha.write(out);
        out.flush();
        out.close();
    }


}
