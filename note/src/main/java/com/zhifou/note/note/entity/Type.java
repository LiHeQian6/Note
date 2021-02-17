package com.zhifou.note.note.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    private String name;
    @ManyToOne(targetEntity = Type.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Type parent;
    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private Set<Type> child;
    @JsonIgnore
    @OneToMany(mappedBy = "type")
    private Set<Note> notes;
    private int status;

}
