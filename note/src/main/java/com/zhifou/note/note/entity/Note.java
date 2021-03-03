package com.zhifou.note.note.entity;

import com.zhifou.note.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;
/*    private int likeNum; //交由redis存储
    private int lookNum;*/
    private Date createTime=new Date();
    @ManyToOne(targetEntity = User.class,cascade = CascadeType.ALL)
    private User user;
    @ManyToOne(targetEntity = Type.class,cascade = CascadeType.ALL)
    @Valid
    @NotNull(message = "必须选择一个分类")
    private Type type;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "notes_tags",
            joinColumns = @JoinColumn(
                    name = "note_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id"))
    @Valid
    @Size(min = 1,message = "至少选择一个标签")
    private Set<Tag> tags;
    private int status=0;

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", user=" + user +
                ", type=" + type +
                ", tags=" + tags +
                ", status=" + status +
                '}';
    }

    public void update(Note newNote) {
        if (newNote.getTitle()!=null) {
            title= newNote.getTitle();
        }
        if (newNote.getContent()!=null) {
            content= newNote.getContent();
        }
        if (newNote.getType()!=null) {
            type= newNote.getType();
        }
        if (newNote.getTags()!=null) {
            tags= newNote.getTags();
        }
    }
}
