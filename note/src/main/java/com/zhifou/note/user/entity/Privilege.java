package com.zhifou.note.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author : li
 * @Date: 2021-01-14 14:18
 */
@Entity(name = "privilege")
@Getter
@Setter
public class Privilege {

    @Id
    @Column(name = "privilege_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "privileges",targetEntity = Role.class)
    private Collection<Role> roles;

    public Privilege(){};

    public Privilege(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Privilege{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
