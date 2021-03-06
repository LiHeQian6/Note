package com.zhifou.note.exception;

import com.zhifou.note.bean.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : li
 * @Date: 2021-02-25 20:48
 */
@Getter
@Setter
public class TokenException extends CustomException {

    public TokenException(String message, Status status) {
        super(message, status);
    }
}
