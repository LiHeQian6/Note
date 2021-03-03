package com.zhifou.note.user.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import com.zhifou.note.exception.bean.UserException;
import com.zhifou.note.exception.bean.ValidateCodeException;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import com.zhifou.note.util.MailUtil;
import com.zhifou.note.util.RedisKeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * @author : li
 * @Date: 2021-01-05 15:05
 */
@Api("用户模块")
@RestController
@Validated
public class UserController {//todo 修改密码，忘记密码，修改用户信息，禁用账户，修改用户角色，添加、修改用户认证，认证管理改认证中心，管理员认证改管理员管理（添加修改管理员角色）

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private MailUtil mailUtil;
    @Resource
    private UserDetailsServiceImp userService;


    @ApiOperation("用户注册")
    @PostMapping("/register/{mailCode}")
    public void userRegister(@Valid @RequestBody User user,@NotNull @PathVariable String mailCode) throws UserException {
        String verifyCode = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getVerifyCodeKey(user.getUsername()));
        if (verifyCode==null) {
            throw new ValidateCodeException("验证码已过期！");
        }
        if (mailCode.equals(verifyCode)) {
            userService.registerUser(user);
        }else {
            throw new ValidateCodeException("验证码不正确");
        }
    }

    @ApiOperation("获取图片验证码")
    @GetMapping(value = "/getImageCode")
    public void geImageCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 40,4,8);
        captcha.setBackground(Color.LIGHT_GRAY);
        //将验证码存储到redis
        redisTemplate.opsForValue().set(RedisKeyUtil.getVerifyCodeKey(request.getRemoteAddr()),captcha.getCode(),5, TimeUnit.MINUTES);
        ServletOutputStream out = response.getOutputStream();
        captcha.write(out);
        out.flush();
        out.close();
    }

    @ApiOperation("获取邮件验证码")
    @GetMapping(value = "/getMailCode")
    public void getMailCode(@RequestParam String mail) throws UnsupportedEncodingException, MessagingException {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(1, 1, 4, 0);
        redisTemplate.opsForValue().set(RedisKeyUtil.getVerifyCodeKey(mail),captcha.getCode());
        mailUtil.sendVerifyCode(mail,captcha.getCode(),"账号注册");
    }


}
