package com.zhifou.note.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

/**
 * @author : li
 * @Date: 2021-01-14 14:18
 */
@Entity
@Getter
@Setter
@Table(uniqueConstraints= @UniqueConstraint(columnNames="name"))
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "权限名不能为空")
    private String name;
    private int status;

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
