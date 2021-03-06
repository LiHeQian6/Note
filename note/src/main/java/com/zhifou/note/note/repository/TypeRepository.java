package com.zhifou.note.note.repository;

import com.zhifou.note.note.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : li
 * @Date: 2021-03-01 15:34
 */
@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
    Type getById(int id);
}
