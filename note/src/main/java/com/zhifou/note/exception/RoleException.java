package com.zhifou.note.exception;

import com.zhifou.note.bean.Status;

/**
 * @author : li
 * @Date: 2021-03-11 22:05
 */
public class RoleException extends CustomException {

    public RoleException(String message, Status status) {
        super(message, status);
    }
}
