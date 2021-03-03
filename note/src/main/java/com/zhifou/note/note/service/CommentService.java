package com.zhifou.note.note.service;

import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 22:13
 */
@Service
@Transactional
public class CommentService {
    @Resource
    private CommentRepository commentRepository;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public Set<Comment> getNoteComments(int noteId){
        return commentRepository.findCommentsByNote_Id(noteId);
    }
}
