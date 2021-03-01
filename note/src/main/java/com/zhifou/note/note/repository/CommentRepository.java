package com.zhifou.note.note.repository;

import com.zhifou.note.note.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : li
 * @Date: 2021-03-01 22:13
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
