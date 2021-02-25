package com.zhifou.note.user.filter;

import cn.hutool.core.util.StrUtil;
import com.zhifou.note.exception.bean.TokenException;
import com.zhifou.note.util.JwtUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter{
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token=resolveToken(request);
            if (StrUtil.isNotBlank(token)) {
                String username = jwtUtils.getUserNameFromToken(token);
                if (StrUtil.isNotBlank(username)) {
                    String refreshToken = jwtUtils.validateToken(token);
                    if (refreshToken != null) {
                        response.setContentType("application/json;charset=UTF-8");
                        response.setHeader("Access-Control-Expose-Headers", "Authorization");
                        response.addHeader("Authorization", "Bearer " + refreshToken);
                        UsernamePasswordAuthenticationToken authentication = jwtUtils.getAuthentication(token);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        //设置当前上下文的认证信息
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }else {
                        resolver.resolveException(request,response,null,new TokenException("Token已经过期,请重新登录！"));
                        return;
                    }
                }

            }
        }
        filterChain.doFilter(request, response);
    }
    private String resolveToken(HttpServletRequest request) {
        // 从请求头中提取 token
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            // 去掉令牌前缀
            return bearerToken.replace("Bearer","");
        }
        return null;
    }
}