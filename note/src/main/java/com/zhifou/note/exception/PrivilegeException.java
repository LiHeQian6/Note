package com.zhifou.note.exception;

import com.zhifou.note.bean.Status;

/**
 * @author : li
 * @Date: 2021-03-11 22:09
 */
public class PrivilegeException extends CustomException {

    public PrivilegeException(String message, Status status) {
        super(message, status);
    }
}
