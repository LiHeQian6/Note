package com.zhifou.note.note.service;

import com.zhifou.note.bean.CommentVO;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.Status;
import com.zhifou.note.exception.CommentException;
import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.repository.CommentRepository;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        Set<Comment> comments = commentRepository.findCommentsByNote_IdAndParentIsNull(noteId);
        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO(comment,
                    likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId()),
                    likeService.findEntityLikeStatus(userId,ENTITY_TYPE_COMMENT,comment.getId()), likeService);
            commentVOs.add(commentVO);
        }
        for (CommentVO vo : commentVOs) {
            for (CommentVO commentVO : vo.getChild()) {
                packageComment(commentVO);
            }
        }
        return commentVOs;
    }
    public Set<CommentVO> getNoteComments(int noteId){
        Set<CommentVO> commentVOs=new HashSet<>();
        Set<Comment> comments = commentRepository.findCommentsByNote_IdAndParentIsNull(noteId);
        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO(comment,
                    likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId()),
                    false, likeService);
            commentVOs.add(commentVO);
        }
        for (CommentVO vo : commentVOs) {
            for (CommentVO commentVO : vo.getChild()) {
                packageComment(commentVO);
            }
        }
        return commentVOs;
    }

    /**
     * @param: comment
     * @return void
     * @description 把评论的子评论放到一级中
     * @author li
     * @Date 2021/4/22 17:00
     */
    public void packageComment(CommentVO comment){
        Set<CommentVO> child = comment.getChild();
        Set<CommentVO> comments = new HashSet<>();
        if (child !=null){
            for (CommentVO vo : child) {
                packageComment(vo);
                if (vo.getChild() != null) {
                    comments.addAll(vo.getChild());
                }
                vo.setChild(null);
                comments.add(vo);
            }
            comment.setChild(comments);
        }
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

    public void deleteComment(int id) throws CommentException {
        Comment comment= getComment(id);
        commentRepository.delete(comment);
    }

    public DataTablesOutput<CommentVO> getComments(DataTablesInput input) {
        DataTablesOutput<CommentVO> output = new DataTablesOutput<>();
        ArrayList<CommentVO> commentVOS = new ArrayList<>();
        DataTablesOutput<Comment> comments = commentRepository.findAll(input);
        List<Comment> data = comments.getData();
        for (Comment comment : data) {
            CommentVO commentVO = new CommentVO(comment,likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId()),false, likeService);
            commentVOS.add(commentVO);
        }
        output.setData(commentVOS);
        output.setRecordsFiltered(comments.getRecordsFiltered());
        output.setRecordsTotal(comments.getRecordsTotal());
        return output;
    }
}
