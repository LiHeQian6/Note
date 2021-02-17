package com.zhifou.note.note.entity;

import com.zhifou.note.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-01-28 22:57
 */
@Entity
@Getter
@Setter
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private int likeNum;
    private int lookNum;
    private Date createTime;
    @ManyToOne(targetEntity = User.class,cascade = CascadeType.ALL)
    private User user;
    @ManyToOne(targetEntity = Type.class,cascade = CascadeType.ALL)
    private Type type;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "notes_tags",
            joinColumns = @JoinColumn(
                    name = "note_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id"))
    private Set<Tag> tags;
    private int status;
}
