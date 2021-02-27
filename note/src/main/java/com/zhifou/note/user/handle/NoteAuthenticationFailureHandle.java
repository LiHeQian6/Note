package com.zhifou.note.user.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhifou.note.bean.ResponseBean;
import com.zhifou.note.bean.Status;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author : li
 * @Date: 2021-02-22 11:11
 */
@Component
public class NoteAuthenticationFailureHandle implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(Status.USER_CREDENTIALS_ERROR);
        responseBean.setMessage(exception.getMessage());
        PrintWriter out = response.getWriter();
        out.write(mapper.writeValueAsString(responseBean));
    }
}
