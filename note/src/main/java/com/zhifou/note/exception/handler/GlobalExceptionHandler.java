package com.zhifou.note.exception.handler;

import com.zhifou.note.bean.ResponseBean;
import com.zhifou.note.bean.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author : li
 * @Date: 2021-01-13 19:26
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseBean HandleException(Exception e){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(Status.COMMON_FAIL);
        responseBean.setMessage(e.getMessage());
        return responseBean;
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseBean defaultErrorHandler(NoHandlerFoundException e){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setMessage(e.getMessage());
        responseBean.setStatus(Status.NOT_FOUND);
        return responseBean;
    }

}
