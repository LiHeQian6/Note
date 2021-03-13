package com.zhifou.note.exception;

import com.zhifou.note.bean.Status;

/**
 * @author : li
 * @Date: 2021-03-08 21:29
 */
public class TagException extends CustomException {

    public TagException(String message, Status status) {
        super(message, status);
    }
}
