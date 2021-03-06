package com.zhifou.note.note.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhifou.note.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-01-29 13:59
 */
@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "评论内容不能为空")
    private String content;
    @Transient
    private int likeNum;
    private Date createTime=new Date();
    @ManyToOne
    @NotNull(message = "评论所属笔记不能为空")
    private Note note;
    @JsonIgnore
    @ManyToOne
    private Comment parent;
    @OneToMany(mappedBy = "parent")
    private Set<Comment> child;
    @ManyToOne
    @NotNull(message = "评论用户不能为空")
    private User user;
    private int status;
}
