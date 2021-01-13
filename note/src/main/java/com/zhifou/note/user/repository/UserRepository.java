package com.zhifou.note.user.repository;

import org.springframework.security.core.userdetails.User;

/**
 * @author : li
 * @Date: 2021-01-10 10:57
 */
//@Repository
public interface UserRepository/* extends JpaRepository<User,Long>*/ {
    User findUserByUsername(String username);
}
