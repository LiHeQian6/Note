package com.zhifou.note.user.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @Author : li
 * @Date: 2021-01-10 11:15
 */
public class CustomerUser extends User {
    public CustomerUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
