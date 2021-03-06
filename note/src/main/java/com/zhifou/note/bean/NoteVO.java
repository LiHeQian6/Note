package com.zhifou.note.bean;

import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.entity.Type;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 22:06
 */
@Getter
@Setter
public class NoteVO {
    private int id;
    private String title;
    private String content;
    private long like;
    private boolean isLike;
    private long look;
    private long collect;
    private boolean isCollect;
    private Set<Comment> comments;
    private Date createTime;
    private UserVO user;
    private Type type;
    private Set<Tag> tags;

    public NoteVO(Note note,int like,boolean isLike,int look,int collect,boolean isCollect,Set<Comment> comments){
        id=note.getId();
        title=note.getTitle();
        content=note.getContent();
        this.like=like;
        this.isLike=isLike;
        this.look=look;
        this.collect=collect;
        this.isCollect=isCollect;
        this.comments=comments;
        createTime=note.getCreateTime();
        user=new UserVO(note.getUser());
        type=note.getType();
        tags=note.getTags();
    }
    public NoteVO(Note note,long like,long look,long collect,Set<Comment> comments){
        id=note.getId();
        title=note.getTitle();
        content=note.getContent();
        this.like=like;
        this.isLike=false;
        this.look=look;
        this.collect=collect;
        this.isCollect=false;
        this.comments=comments;
        createTime=note.getCreateTime();
        user=new UserVO(note.getUser());
        type=note.getType();
        tags=note.getTags();
    }
    public NoteVO(Note note){
        id=note.getId();
        title=note.getTitle();
        content=note.getContent();
        createTime=note.getCreateTime();
        user=new UserVO(note.getUser());
        type=note.getType();
        tags=note.getTags();
    }

    public void setLike(long like) {
        this.like = like;
    }

    public void setCollect(long collect) {
        this.collect = collect;
    }

    public void setIsLike(boolean like) {
        isLike = like;
    }

    public void setIsCollect(boolean collect) {
        isCollect = collect;
    }

    public boolean isLike() {
        return isLike;
    }

    public boolean isCollect() {
        return isCollect;
    }
}
