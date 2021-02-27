package com.zhifou.note.exception.handler;

import com.zhifou.note.bean.ResponseBean;
import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.bean.TokenException;
import com.zhifou.note.exception.bean.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(UserException.class)
    public ResponseBean UserException(UserException e){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(e.getStatus());
        responseBean.setMessage(e.getMessage());
        return responseBean;
    }

    @ExceptionHandler(TokenException.class)
    public ResponseBean TokenException(TokenException e){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(e.getStatus());
        responseBean.setMessage(e.getMessage());
        return responseBean;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseBean exception(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();
        for (ObjectError error : e.getAllErrors()) {
            errors.append(error.getDefaultMessage()).append(";");
        }
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(Status.PARAM_NOT_VALID);
        responseBean.setMessage(errors.toString());
        return responseBean;
    }

    @ExceptionHandler(Exception.class)
    public ResponseBean HandleException(Exception e){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(Status.COMMON_FAIL);
        responseBean.setMessage(e.getMessage());
        return responseBean;
    }

    @ExceptionHandler
    public ResponseBean defaultErrorHandler(NoHandlerFoundException e){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus(Status.NOT_FOUND);
        responseBean.setMessage(e.getMessage());
        return responseBean;
    }

}
