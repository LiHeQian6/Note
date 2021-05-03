package com.zhifou.note.user.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import com.zhifou.note.bean.RegisterValid;
import com.zhifou.note.bean.UserVO;
import com.zhifou.note.exception.CertificationException;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.exception.ValidateCodeException;
import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.user.entity.Certification;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.CertificationService;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import com.zhifou.note.util.JwtUtils;
import com.zhifou.note.util.MailUtil;
import com.zhifou.note.util.RedisKeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
public class UserController {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private MailUtil mailUtil;
    @Resource
    private UserDetailsServiceImp userService;
    @Resource
    private LikeService likeService;
    @Resource
    private CertificationService certificationService;
    @Resource
    private JwtUtils jwtUtils;


    @ApiOperation("用户注册")
    @PostMapping("/register/{mailCode}")
    public void userRegister( @RequestBody @Validated(value = RegisterValid.class) User user,
                             @NotNull @PathVariable String mailCode) throws UserException {
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
    @GetMapping(value = "/getMailCode/{option}")
    public void getMailCode(@RequestParam String mail,@ApiParam("要进行的操作") @PathVariable String option) throws UnsupportedEncodingException, MessagingException {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(1, 1, 4, 0);
        redisTemplate.opsForValue().set(RedisKeyUtil.getVerifyCodeKey(mail),captcha.getCode(),5, TimeUnit.MINUTES);
        mailUtil.sendVerifyCode(mail,captcha.getCode(),option);
    }

    @ApiOperation("忘记密码")
    @PostMapping("/forgotPassword/{mailCode}")
    public void forgotPassword(@NotBlank(message = "邮箱不能为空") String username,@NotBlank(message = "密码不能为空") String newPassword,@PathVariable String mailCode) throws UserException {
        String verifyCode = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getVerifyCodeKey(username));
        if (verifyCode==null) {
            throw new ValidateCodeException("验证码已过期！");
        }
        if (mailCode.equals(verifyCode)) {
            userService.resetPassword(username,newPassword);
        }else {
            throw new ValidateCodeException("验证码不正确");
        }
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getInfo")
    public UserVO getUserInfo() throws UserException {
        User userInfo = jwtUtils.getUserInfo();
        UserVO userVO = new UserVO(userInfo);
        userVO.setLike(likeService.findUserLikeCount(userInfo.getId()));
        return userVO;
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/changeInfo")
    public void changeInfo(@RequestBody @Valid @ApiParam("只能修改密码、昵称、简介") User user) throws UserException {
        User userInfo = jwtUtils.getUserInfo();
        if (userInfo.update(user)) {
            userService.updateUserInfo(userInfo);
        }
    }

    @ApiOperation("用户认证")
    @PostMapping("/certification")
    public void certification(@Valid Certification certification) throws CertificationException {
        User userInfo = jwtUtils.getUserInfo();
        certificationService.certification(userInfo,certification);
    }

    @ApiOperation("分页获取热门作者")
    @GetMapping("/users/popular")
    public Page<UserVO> getPopularUsers(@ApiParam("第几页") @Min(value = 0, message = "页数最小为0") int page,
                                        @ApiParam("页大小") @Min(value = 1, message = "页尺寸最小为1") int size){
        int id=0;
        User userInfo = jwtUtils.getUserInfo();
        if (userInfo !=null) {
            id=userInfo.getId();
        }
        return userService.getUsersByPopular(id,page,size);
    }



}
