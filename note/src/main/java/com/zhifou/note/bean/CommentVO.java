package com.zhifou.note.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.note.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-06 21:42
 */
@Getter
@Setter
public class CommentVO implements Constant {
    private int id;
    private String content;
    private long likeNum;
    private boolean isLike;
    private Date createTime;
    private Set<CommentVO> child;
    private UserVO user;
    @JsonIgnore
    @Resource
    private LikeService likeService;

    public CommentVO(Comment comment,long like,boolean isLike) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.likeNum = like;
        this.isLike = isLike;
        this.createTime = comment.getCreateTime();
        this.child = getChildVO(comment.getChild(),comment.getUser().getId());
        this.user =new UserVO(comment.getUser());
    }

    private Set<CommentVO> getChildVO(Set<Comment> child,int userId) {
        Set<CommentVO> commentVOs=new HashSet<>();
        for (Comment comment : child) {
            CommentVO commentVO = new CommentVO(comment,
                    likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId()),
                    likeService.findEntityLikeStatus(userId,ENTITY_TYPE_COMMENT,comment.getId()));
            commentVOs.add(commentVO);
        }
        return commentVOs;
    }

}
