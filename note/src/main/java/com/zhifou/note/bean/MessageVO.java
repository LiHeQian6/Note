package com.zhifou.note.bean;

import com.zhifou.note.message.entity.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author : li
 * @Date: 2021-03-27 17:45
 */
@Setter
@Getter
public class MessageVO {
    private int id;
    private UserVO from;
    private UserVO to;//系统自定义时为null，表示发送给所有用户
    private String msType;//消息的类型，用户和用户间发送的则为{小id_大id}，若为系统发送的则为like,follow,comment,collect,系统自定义为system
    private String content;//消息的内容用json格式来存储
    private Date createTime;

    public MessageVO(){}
    public MessageVO(Message message){
        id=message.getId();
        from=new UserVO(message.getFrom());
        to=new UserVO(message.getTo());
        msType=message.getMsType();
        content=message.getContent();
        createTime=message.getCreateTime();
    }

}
