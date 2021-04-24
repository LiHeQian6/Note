package com.zhifou.note.note.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-01-28 23:05
 */
@Entity
@Getter
@Setter
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "类型名不能为空！")
    private String name;
    @ManyToOne(targetEntity = Type.class,cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "parent_id")
    private Type parent;
    @OneToMany(mappedBy = "parent")
    private Set<Type> child;
    @JsonIgnore
    @OneToMany(mappedBy = "type")
    private Set<Note> notes;
    @JsonIgnore
    private int status=0;

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", child=" + child +
                ", status=" + status +
                '}';
    }
}
