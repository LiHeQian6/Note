package com.zhifou.note.exception;

import com.zhifou.note.bean.Status;

/**
 * @author : li
 * @Date: 2021-03-23 18:29
 */
public class CertificationException extends CustomException {
    public CertificationException(String message, Status status) {
        super(message, status);
    }
}
