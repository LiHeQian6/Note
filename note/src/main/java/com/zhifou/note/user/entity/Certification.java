package com.zhifou.note.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


/**
 * @author : li
 * @Date: 2021-03-18 11:26
 */
@Entity
@Getter
@Setter
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "学号不能为空！")
    private String num;
    @NotBlank(message = "姓名不能为空！")
    private String name;

    @OneToOne
    @JsonIgnore
    private User user;

    public Certification() {
    }

    public Certification(@NotBlank(message = "学号不能为空！") String num, @NotBlank(message = "姓名不能为空！") String name) {
        this.num = num;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Certification{" +
                "name='" + name + '\'' +
                '}';
    }
}
