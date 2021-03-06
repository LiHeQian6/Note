package com.zhifou.note.note.controller;

import com.zhifou.note.exception.NoteException;
import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.service.CommentService;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author : li
 * @Date: 2021-03-01 22:12
 */
@RestController
public class CommentController {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private NoteService noteService;
    @Resource
    private CommentService commentService;

    @ApiOperation("发布评论")
    @PostMapping("/comment")
    public void publishComment(@Valid @RequestBody @ApiParam("只需要传content,note.id,user.id") Comment comment) throws NoteException {
        User userInfo = jwtUtils.getUserInfo();
        comment.setUser(userInfo);
        Note note = comment.getNote();
        comment.setNote(noteService.getNote(note.getId()));
        commentService.addComment(comment);
    }
    //todo 评论修改、删除
}
