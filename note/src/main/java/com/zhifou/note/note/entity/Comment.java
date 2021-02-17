package com.zhifou.note.note.entity;

import com.zhifou.note.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    private String content;
    private int likeNum;
    private Date createTime=new Date();
    @ManyToOne(cascade = CascadeType.ALL)
    private Note note;
    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "parent_id")
    private Comment parent;
    @OneToMany(mappedBy = "parent")
    private Set<Comment> child;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    private int status;
}
