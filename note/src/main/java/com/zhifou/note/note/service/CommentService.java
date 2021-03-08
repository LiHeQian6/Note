package com.zhifou.note.note.service;

import com.zhifou.note.bean.CommentVO;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.CommentException;
import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-03-01 22:13
 */
@Service
@Transactional
public class CommentService implements Constant {
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private LikeService likeService;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public Set<CommentVO> getNoteComments(int noteId,int userId){
        Set<CommentVO> commentVOs=new HashSet<>();
        Set<Comment> comments = commentRepository.findCommentsByNote_Id(noteId);
        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO(comment,
                    likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId()),
                    likeService.findEntityLikeStatus(userId,ENTITY_TYPE_COMMENT,comment.getId()));
            commentVOs.add(commentVO);
        }
        return commentVOs;
    }

    public Comment getComment(int id) throws CommentException {
        Comment comment = commentRepository.findNoteById(id);
        if (comment ==null) {
            throw new CommentException("没有找到指定评论！", Status.NOT_FOUND_COMMENT);
        }
        return comment;
    }
    public Comment getComment(int id, String username) throws CommentException {
        Comment comment= getComment(id);
        if (comment.getUser().getUsername().equals(username)) {
            return comment;
        }
        throw new CommentException("没有找到指定评论！", Status.NOT_FOUND_COMMENT);
    }

    public void updateComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(int id, String username) throws CommentException {
        Comment comment= getComment(id, username);
        commentRepository.delete(comment);
    }
}
