package com.zhifou.note.message.entity;

import com.zhifou.note.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : li
 * @Date: 2021-02-03 19:30
 */
@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User from;
    @ManyToOne
    private User to;//系统自定义时为null，表示发送给所有用户
    private String msType;//消息的类型，用户和用户间发送的则为{小id_大id}，若为系统发送的则为like,follow,comment,collect,系统自定义为system
    private String content;//消息的内容用json格式来存储
    private Date createTime=new Date();
    private int status=0;//0表示可用1表示删除
}
