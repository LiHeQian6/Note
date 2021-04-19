package com.zhifou.note.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.entity.Type;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
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
    private Set<CommentVO> comments;
    private String createTime;
    private UserVO user;
    private Type type;
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Set<Tag> tags;

    //笔记详情页使用
    public NoteVO(Note note,int like,boolean isLike,int look,int collect,boolean isCollect,Set<CommentVO> comments){
        id=note.getId();
        title=note.getTitle();
        content=note.getContent().substring(0, Math.min(note.getContent().length(), 100));
        this.like=like;
        this.isLike=isLike;
        this.look=look;
        this.collect=collect;
        this.isCollect=isCollect;
        this.comments=comments;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        createTime= format.format(note.getCreateTime());
        user=new UserVO(note.getUser());
        type=note.getType();
        tags=note.getTags();
    }

    //游客笔记详情页使用
    public NoteVO(Note note,long like,long look,long collect,Set<CommentVO> comments){
        id=note.getId();
        title=note.getTitle();
        content=note.getContent().substring(0, Math.min(note.getContent().length(), 100));
        this.like=like;
        this.isLike=false;
        this.look=look;
        this.collect=collect;
        this.isCollect=false;
        this.comments=comments;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        createTime= format.format(note.getCreateTime());
        user=new UserVO(note.getUser());
        type=note.getType();
        tags=note.getTags();
    }

    //笔记列表展示使用
    public NoteVO(Note note){
        id=note.getId();
        title=note.getTitle();
        content=note.getContent().substring(0, Math.min(note.getContent().length(), 100));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        createTime= format.format(note.getCreateTime());
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
