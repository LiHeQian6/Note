package com.zhifou.note.aspect;

import com.zhifou.note.bean.ResponseBean;
import com.zhifou.note.bean.Status;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author : li
 * @Date: 2021-02-06 22:26
 */
@Component
@ControllerAdvice("com.zhifou.note")
public class ResultAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ResponseBean bean = new ResponseBean();
        if (body instanceof ResponseBean){
            return body;
        }else {
            bean.setData(body);
            bean.setStatus(Status.CLIENT_SUCCESS);
            bean.setMessage(Status.CLIENT_SUCCESS.getMessage());
        }
        return bean;
    }


}