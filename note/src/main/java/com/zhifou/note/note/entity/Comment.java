package com.zhifou.note.note.entity;

import com.zhifou.note.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
    @Length(max = 225,message = "评论长度不能超过255个字符")
    private String content;
    @Transient
    private int likeNum;
    private Date createTime=new Date();
    @ManyToOne
    @NotNull(message = "评论所属笔记不能为空")
    private Note note;
//    @JsonIgnore
    @ManyToOne
    private Comment parent;
    @OneToMany(mappedBy = "parent")
    private Set<Comment> child;
    @ManyToOne
    private User user;
    private int status;

    public boolean update(Comment newComment) {
        if (newComment.getContent()!=null) {
            content= newComment.getContent();
            return true;
        }
        return false;
    }
}
