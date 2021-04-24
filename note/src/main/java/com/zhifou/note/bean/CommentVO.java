package com.zhifou.note.bean;

import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.note.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
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
    private UserVO to;
    private UserVO user;
    private Set<CommentVO> child;
    private String createTime;

    public CommentVO() {
    }

    public CommentVO(Comment comment, long like, boolean isLike, LikeService likeService) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.likeNum = like;
        this.isLike = isLike;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.createTime = format.format(comment.getCreateTime());
        this.child = getChildVO(comment.getChild(),comment.getUser().getId(), likeService);
        Comment parent = comment.getParent();
        if (parent!=null) {
            this.to =new UserVO(parent.getUser());
        }
        this.user =new UserVO(comment.getUser());
    }

    private Set<CommentVO> getChildVO(Set<Comment> child, int userId, LikeService likeService) {
        if (child==null){
            return null;
        }
        Set<CommentVO> commentVOs=new HashSet<>();
        for (Comment comment : child) {
            CommentVO commentVO = new CommentVO(comment,
                    likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId()),
                    likeService.findEntityLikeStatus(userId,ENTITY_TYPE_COMMENT,comment.getId()), likeService);
            commentVOs.add(commentVO);
        }
        return commentVOs;
    }

}
