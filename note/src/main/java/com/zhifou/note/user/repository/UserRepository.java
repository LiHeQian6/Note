package com.zhifou.note.user.repository;

import com.zhifou.note.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : li
 * @Date: 2021-01-10 10:57
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByUsername(String username);
    boolean existsByUsername(String username);
}
