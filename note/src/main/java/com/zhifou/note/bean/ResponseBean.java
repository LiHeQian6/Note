package com.zhifou.note.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : li
 * @Date: 2021-01-13 21:00
 */
@Data
public class ResponseBean  implements Serializable {
    private Status status;
    private Object data;
}
