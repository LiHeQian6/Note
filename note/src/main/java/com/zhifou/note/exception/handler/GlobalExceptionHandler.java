package com.zhifou.note.exception.handler;

import com.zhifou.note.bean.ResponseBean;
import com.zhifou.note.bean.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : li
 * @Date: 2021-01-13 19:26
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseBean HandleException(){

        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(Status.OK);
        return responseBean;
    }

}
