package com.zhifou.note.note.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhifou.note.annotation.WordFilter;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.exception.CommentException;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.message.event.EventProducer;
import com.zhifou.note.message.event.MessageEvent;
import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.service.CommentService;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author : li
 * @Date: 2021-03-01 22:12
 */
@RestController
public class CommentController implements Constant {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private NoteService noteService;
    @Resource
    private CommentService commentService;
    @Resource
    private EventProducer eventProducer;

    @WordFilter(description = "comment")
    @ApiOperation("发布评论")
    @PostMapping("/comment")
    public void publishComment(@Valid @RequestBody @ApiParam("只需要传content,note.id;有parent传parent.Id") Comment comment) throws NoteException, JsonProcessingException, CommentException {
        User userInfo = jwtUtils.getUserInfo();
        comment.setUser(userInfo);
        Note note = comment.getNote();
        commentService.addComment(comment);
        MessageEvent commentEvent = new MessageEvent();
        commentEvent.setUserId(userInfo.getId());
        int targetId;
        if (comment.getParent()==null) {
            commentEvent.setEntityType(ENTITY_TYPE_NOTE);
            commentEvent.setEntityId(note.getId());
            targetId=noteService.getNote(comment.getNote().getId()).getUser().getId();
        }else {
            commentEvent.setEntityType(ENTITY_TYPE_COMMENT);
            commentEvent.setEntityId(comment.getParent().getId());
            targetId=commentService.getComment(comment.getParent().getId()).getUser().getId();
            commentEvent.setExtra("noteId",comment.getNote().getId());
        }
        commentEvent.setEntityUserId(targetId);
        commentEvent.setTopic(TOPIC_COMMENT);
        eventProducer.fireMessageEvent(commentEvent);
    }

    @WordFilter(description = "comment")
    @ApiOperation("修改评论")
    @PutMapping("/comment")
    public void editComment(@Valid @RequestBody Comment newComment) throws CommentException {
        User userInfo = jwtUtils.getUserInfo();
        Comment comment = commentService.getComment(newComment.getId(),userInfo.getUsername());
        if (comment.update(newComment)) {
            commentService.updateComment(comment);
        }
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/comment/{id}")
    public void deleteComment(@PathVariable int id) throws CommentException {
        User userInfo = jwtUtils.getUserInfo();
        commentService.deleteComment(id,userInfo.getUsername());
    }

}
