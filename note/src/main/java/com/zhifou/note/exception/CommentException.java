package com.zhifou.note.exception;

import com.zhifou.note.bean.Status;

/**
 * @author : li
 * @Date: 2021-03-06 23:01
 */
public class CommentException extends CustomException {
    public CommentException(String message, Status status) {
        super(message, status);
    }
}
