package com.zhifou.note.note.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-01-29 11:01
 */
@Entity
@Getter
@Setter
@Table(uniqueConstraints= @UniqueConstraint(columnNames="name"))
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "标签名称不能为空")
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Note> notes;
    @JsonIgnore
    private int status;

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
