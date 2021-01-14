package com.zhifou.note.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author : li
 * @Date: 2021-01-10 11:15
 */
@Entity(name="user")
@Getter
@Setter
public class User extends org.springframework.security.core.userdetails.User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String introduction="还没有任何介绍哦！";
    @Column
    private String nick_name="一只笔记君";
    @Column
    private String photo;
    @Column
    private Date createTime=new Date();
    @Column
    private Date lastReadTime=new Date();
    @Column
    private boolean accountNonExpired=true;
    @Column
    private boolean accountNonLocked=true;
    @Column
    private boolean credentialsNonExpired=true;
    @Column
    private boolean enabled=true;
    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
                name = "user_id"),
        inverseJoinColumns = @JoinColumn(
                name = "role_id"))
    private List<Role> roles;

    private User() {
        super("admin","admin",new ArrayList<>());
    }

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
        this.password = password;
//        this.roles=  TODO 修改role和privilege去掉中文，添加映射
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities= new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
