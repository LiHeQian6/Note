package com.zhifou.note.user.filter;

import com.zhifou.note.exception.bean.ValidateCodeException;
import com.zhifou.note.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException{
        if ("/admin/login".equalsIgnoreCase(httpServletRequest.getRequestURI())
                && "post".equalsIgnoreCase(httpServletRequest.getMethod())) {
//                HttpSession session = httpServletRequest.getSession();
            String codeInRedis = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getVerifyCodeKey(httpServletRequest.getRemoteAddr()));
            String codeInReq = httpServletRequest.getParameter("imageCode");
            try {
                validateCode(codeInRedis,codeInReq);
            } catch (Exception e) {
                httpServletRequest.setAttribute("error",e.getMessage());
                httpServletRequest.getRequestDispatcher("/admin/login").forward(httpServletRequest,httpServletResponse);
                redisTemplate.delete(RedisKeyUtil.getVerifyCodeKey(httpServletRequest.getRemoteAddr()));
                return;
            }
            redisTemplate.delete(RedisKeyUtil.getVerifyCodeKey(httpServletRequest.getRemoteAddr()));
        }else if("/login".equalsIgnoreCase(httpServletRequest.getRequestURI())
                && "post".equalsIgnoreCase(httpServletRequest.getMethod())){
            String codeInRedis = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getVerifyCodeKey(httpServletRequest.getRemoteAddr()));
//            HttpSession session = httpServletRequest.getSession();
            String codeInReq = httpServletRequest.getParameter("imageCode");
            try {
                validateCode(codeInRedis,codeInReq);
            } catch (Exception e) {
                redisTemplate.delete(RedisKeyUtil.getVerifyCodeKey(httpServletRequest.getRemoteAddr()));
                resolver.resolveException(httpServletRequest,httpServletResponse,null,e);
                return;
            }
            redisTemplate.delete(RedisKeyUtil.getVerifyCodeKey(httpServletRequest.getRemoteAddr()));
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validateCode(String codeInRedis,String codeInRequest){
//        String codeInSession = (String)session.getAttribute("code");

        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空！");
        }
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码已经过期！");
        }
        if (!codeInRequest.equalsIgnoreCase(codeInRedis)) {
            throw new ValidateCodeException("验证码不正确！");
        }
    }

}