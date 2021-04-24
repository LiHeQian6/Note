package com.zhifou.note.note.repository;

import com.zhifou.note.note.entity.Comment;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 22:13
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer>, DataTablesRepository<Comment,Integer> {
    Set<Comment> findCommentsByNote_Id(int noteId);
    Comment findNoteById(int id);
}
