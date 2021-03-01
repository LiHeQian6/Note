package com.zhifou.note.bean;

import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.entity.Type;
import com.zhifou.note.user.entity.User;

import java.util.Date;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 22:06
 */
public class NoteVO {
    private int id;
    private String title;
    private String content;
    private int like;
    private int look;
    private int collect;
    private Set<Comment> comments;
    private Date createTime=new Date();
    private User user;
    private Type type;
    private Set<Tag> tags;
}
