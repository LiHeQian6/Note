package com.zhifou.note.exception.bean;

import com.zhifou.note.bean.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : li
 * @Date: 2021-03-02 16:28
 */
@Getter
@Setter
public class NoteException extends CustomException {

    public NoteException(String message, Status status) {
        super(message, status);
    }
}
