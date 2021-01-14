package com.zhifou.note.user.repository;

import com.zhifou.note.user.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : li
 * @Date: 2021-01-14 20:01
 */
public interface PrivilegeRepository extends JpaRepository<Privilege,Integer> {
    Privilege findByName(String name);
}
