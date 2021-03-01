package com.zhifou.note.exception.bean;

import com.zhifou.note.bean.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : li
 * @Date: 2021-02-27 21:55
 */
@Getter
@Setter
public class UserException extends Exception{
    private Status status;
    public UserException(String message,Status status) {
        super(message);
        this.status=status;
    }
}
