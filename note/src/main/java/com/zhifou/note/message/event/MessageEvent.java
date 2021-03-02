package com.zhifou.note.message.event;

import lombok.Data;

import java.util.HashMap;

/**
 * @author : li
 * @Date: 2021-02-28 16:19
 */
@Data
public class MessageEvent {
    private String topic;
    private int userId;
    private int entityId;
    private int entityType;
    private int entityUserId;
    private HashMap<String,Object> extra=new HashMap<>();

    public void setExtra(String key,Object value) {
        this.extra.put(key,value);
    }
}
