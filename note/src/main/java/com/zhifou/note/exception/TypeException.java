package com.zhifou.note.exception;

import com.zhifou.note.bean.Status;

/**
 * @author : li
 * @Date: 2021-03-10 22:09
 */
public class TypeException extends CustomException {
    public TypeException(String message, Status status) {
        super(message, status);
    }
}
