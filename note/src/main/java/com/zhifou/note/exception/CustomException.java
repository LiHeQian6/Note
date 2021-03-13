package com.zhifou.note.exception;

import com.zhifou.note.bean.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : li
 * @Date: 2021-03-02 16:36
 */
@Getter
@Setter
public class CustomException extends Exception {
    private Status status;
    public CustomException(String message,Status status) {
        super(message);
        this.status=status;
    }
}
