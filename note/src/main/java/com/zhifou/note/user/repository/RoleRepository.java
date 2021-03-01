package com.zhifou.note.user.repository;

import com.zhifou.note.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : li
 * @Date: 2021-01-14 19:59
 */
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(String name);
}
