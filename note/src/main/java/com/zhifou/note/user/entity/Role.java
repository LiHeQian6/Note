package com.zhifou.note.user.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "role")
@Data
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "name_zh")
    private String name_zh;
    @ManyToMany(mappedBy = "roles",targetEntity = User.class)
    private Collection<org.springframework.security.core.userdetails.User> users;
    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id"))
    private Collection<Privilege> privileges;

    public Role() {
    }

    public Role(String name, String name_zh) {
        this.name = name;
        this.name_zh = name_zh;
    }
}