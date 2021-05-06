package com.zhifou.note.note.repository;

import com.zhifou.note.note.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : li
 * @Date: 2021-03-01 15:39
 */
@Repository
public interface TagRepository extends JpaRepository<Tag,Integer>, DataTablesRepository<Tag,Integer> {
    Page<Tag> findTagsByStatus(int status, Pageable pageable);

    boolean existsByName(String name);

    Tag findTagByName(String name);

    Tag findTagById(int id);

    List<Tag> findAllByNameIgnoreCaseIsContaining(String name);
}
