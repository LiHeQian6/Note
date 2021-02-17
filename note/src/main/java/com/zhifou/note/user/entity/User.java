package com.zhifou.note.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhifou.note.note.entity.Note;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.*;

/**
 * @author : li
 * @Date: 2021-01-10 11:15
 */
@Entity
@Getter
@Setter
public class User extends org.springframework.security.core.userdetails.User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String introduction="还没有任何介绍哦！";
    private String nickName="一只笔记君";
    private String photo;
    private String certification;//实名认证，保存真实名字
    private Date createTime=new Date();
    private Date lastReadTime=new Date();
    private boolean accountNonExpired=true;
    private boolean accountNonLocked=true;
    private boolean credentialsNonExpired=true;
    private boolean enabled=true;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
                name = "user_id"),
        inverseJoinColumns = @JoinColumn(
                name = "role_id"))
    private List<Role> roles;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Note> notes;


    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public User() {
        super("admin1","admin",new ArrayList<>());
    }

    public User(String username, String password, List<Role> roles, Collection<? extends GrantedAuthority> authorities) {
        super(username, password,authorities);
        this.username = username;
        this.password = password;
        this.roles= roles;
        this.authorities=authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        return authorities;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + "[PROTECTED]" + '\'' +
                ", introduction='" + introduction + '\'' +
                ", nick_name='" + nickName + '\'' +
                ", photo='" + photo + '\'' +
                ", createTime=" + createTime +
                ", lastReadTime=" + lastReadTime +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", authorities=" + authorities +
                '}';
    }
}
