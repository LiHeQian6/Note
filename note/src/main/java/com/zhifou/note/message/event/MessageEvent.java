package com.zhifou.note.message.event;

import lombok.Data;

import java.util.HashMap;

/**
 * @author : li
 * @Date: 2021-02-28 16:19
 */
@Data
public class MessageEvent {
    private String topic;//事件类型
    private int userId; //事件发起者id
    private int entityId; //事件触发对象id
    private int entityType; //事件触发对象类型；笔记（点赞、评论），评论（点赞，评论），用户（关注）
    private int entityUserId;//事件接收者id
    private HashMap<String,Object> extra=new HashMap<>();

    public void setExtra(String key,Object value) {
        this.extra.put(key,value);
    }
}
