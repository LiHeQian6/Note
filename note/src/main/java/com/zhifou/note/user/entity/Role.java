package com.zhifou.note.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table(uniqueConstraints= @UniqueConstraint(columnNames="name"))
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "角色名称不能为空")
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "roles",targetEntity = User.class)
    private Collection<org.springframework.security.core.userdetails.User> users;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id"))
    private Collection<Privilege> privileges;
    private int status; //角色状态，0表示正常，1表示已经删除

    public Role() {
    }

    public Role(String name, Collection<Privilege> privileges) {
        this.name = name;
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", privileges=" + privileges +
                '}';
    }

    @Override
    public String getAuthority() {
        return name;
    }
}