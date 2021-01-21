package com.zhifou.note.user.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author : li
 * @Date: 2021-01-14 14:18
 */
@Entity(name = "privilege")
@Data
public class Privilege {

    @Id
    @Column(name = "privilege_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "name_zh")
    private String name_zh;

    @ManyToMany(mappedBy = "privileges",targetEntity = Role.class)
    private Collection<Role> roles;

    public Privilege(){};

    public Privilege(String name, String name_zh) {
        this.name = name;
        this.name_zh = name_zh;
    }
}
