package com.zhifou.note.exception;

import com.zhifou.note.bean.Status;

/**
 * @author : li
 * @Date: 2021-04-08 17:37
 */
public class MessageException extends CustomException {

    public MessageException(String message, Status status) {
        super(message, status);
    }
}
