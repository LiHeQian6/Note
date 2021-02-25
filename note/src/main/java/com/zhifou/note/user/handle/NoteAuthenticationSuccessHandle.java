package com.zhifou.note.user.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhifou.note.bean.ResponseBean;
import com.zhifou.note.bean.Status;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import com.zhifou.note.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;


/**
 * @author : li
 * @Date: 2021-02-22 10:46
 */
@Component
public class NoteAuthenticationSuccessHandle extends SavedRequestAwareAuthenticationSuccessHandler {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Value("${jwt.expiration}")
    private Long expiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User userDetails = (User)authentication.getPrincipal();//拿到登录用户信息
        String jwtToken = jwtUtils.generateToken(userDetails);//生成token
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Authorization", "Bearer " + jwtToken);
        redisTemplate.opsForValue().set(RedisKeyUtil.getTokenKey(jwtUtils.getUserNameFromToken(jwtToken)),
                jwtToken,expiration, TimeUnit.SECONDS);
        ObjectMapper mapper = new ObjectMapper();
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(Status.SUCCESS);
        responseBean.setMessage(Status.SUCCESS.getMessage());
        PrintWriter out = response.getWriter();
        out.write(mapper.writeValueAsString(responseBean));
    }
}
