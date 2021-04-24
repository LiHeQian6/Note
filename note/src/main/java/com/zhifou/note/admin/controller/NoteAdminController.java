package com.zhifou.note.admin.controller;

import com.zhifou.note.bean.CommentVO;
import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.exception.CommentException;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.note.service.CommentService;
import com.zhifou.note.note.service.NoteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author : li
 * @Date: 2021-02-08 18:28
 */
@RestController
@Validated
@RequestMapping("/admin")
public class NoteAdminController {

    @Resource
    private NoteService noteService;
    @Resource
    private CommentService commentService;


    @ApiOperation("分页获取所有笔记")
    @GetMapping("/notes")
    public DataTablesOutput<NoteVO> getNotes(@Valid DataTablesInput input){
        input.getColumns().remove(input.getColumns().size()-1);
        return noteService.getNotes(input);
    }

    @ApiOperation("删除笔记")
    @DeleteMapping("/note/{id}")
    public String deleteNote(@PathVariable int id) {
        try {
            noteService.deleteNote(id);
        } catch (NoteException e) {
            return e.getMessage();
        }
        return "true";
    }

    @ApiOperation("分页获取所有评论")
    @GetMapping("/comments")
    public DataTablesOutput<CommentVO> getComments(@Valid DataTablesInput input){
        input.getColumns().remove(input.getColumns().size()-1);
        input.getColumns().remove(input.getColumn("likeNum"));
        return commentService.getComments(input);
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/comment/{id}")
    public String deleteComment(@PathVariable int id) {
        try {
            commentService.deleteComment(id);
        } catch (CommentException e) {
            return e.getMessage();
        }
        return "true";
    }

}
