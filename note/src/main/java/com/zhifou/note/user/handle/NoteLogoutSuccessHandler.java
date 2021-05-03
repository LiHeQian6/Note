package com.zhifou.note.user.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhifou.note.bean.ResponseBean;
import com.zhifou.note.bean.Status;
import com.zhifou.note.util.JwtUtils;
import com.zhifou.note.util.RedisKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author : li
 * @Date: 2021-04-23 21:09
 */
@Component
public class NoteLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private JwtUtils jwtUtils;

    @Override//todo 测试
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token=resolveToken(request);
        redisTemplate.delete(RedisKeyUtil.getTokenKey(jwtUtils.getUserNameFromToken(token)));
        ObjectMapper mapper = new ObjectMapper();
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(Status.SUCCESS);
        responseBean.setMessage(Status.SUCCESS.getMessage());
        PrintWriter out = response.getWriter();
        out.write(mapper.writeValueAsString(responseBean));
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
