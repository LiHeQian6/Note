package com.zhifou.note.note.repository;

import com.zhifou.note.note.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 15:39
 */
@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> {
    Set<Tag> findTagsByStatus(int status);

    boolean existsByName(String name);
}
