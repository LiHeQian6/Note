package com.zhifou.note.user.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhifou.note.bean.ResponseBean;
import com.zhifou.note.bean.Status;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author : li
 * @Date: 2021-02-22 10:46
 */
@Component
public class NoteAuthenticationSuccessHandle extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(Status.SUCCESS);
        responseBean.setMessage(Status.SUCCESS.getMessage());
        PrintWriter out = response.getWriter();
        out.write(mapper.writeValueAsString(responseBean));
    }
}
