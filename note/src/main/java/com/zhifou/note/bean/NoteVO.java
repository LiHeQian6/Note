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
    private long look;
    private long collect;
    private long commentNum;
    private long popularity;
    private boolean isLiked;
    private boolean isCollected;
    private Set<CommentVO> comments;
    private String createTime;
    private UserVO user;
    private Type type;
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Set<Tag> tags;


    //统计笔记评论数
    private long countComments() {
        long num=comments.size();
        for (CommentVO comment : comments) {
            Set<CommentVO> child = comment.getChild();
            if (child!=null) {
                num+= child.size();
            }
        }
        return num;
    }

    //游客笔记详情页使用
    public NoteVO(Note note,long like,long look,long collect,Set<CommentVO> comments){
        id=note.getId();
        title=note.getTitle();
        content=note.getContent();
        this.like=like;
        this.isLiked=false;
        this.look=look;
        this.collect=collect;
        this.isCollected=false;
        this.comments=comments;
        this.commentNum=countComments();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        createTime= format.format(note.getCreateTime());
        user=new UserVO(note.getUser());
        type=note.getType();
        tags=note.getTags();
    }

    //后台管理系统笔记列表展示使用
    public NoteVO(Note note){
        id=note.getId();
        title=note.getTitle();
        content=note.getContent();
        this.commentNum=countComments();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        createTime= format.format(note.getCreateTime());
        user=new UserVO(note.getUser());
        type=note.getType();
        tags=note.getTags();
    }

}
