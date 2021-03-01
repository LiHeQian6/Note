package com.zhifou.note.note.controller;

import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.entity.Tag;
import com.zhifou.note.note.entity.Type;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.note.service.TagService;
import com.zhifou.note.note.service.TypeService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-02-08 17:23
 */
@Api("笔记模块api")
@RestController
public class NoteController {
    @Resource
    private NoteService noteService;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private UserDetailsServiceImp userDetailsServiceImp;

    @ApiOperation("浏览笔记")
    @GetMapping("/note/{noteId}")
    public NoteVO browseNote(@PathVariable int noteId){
//        return noteService.getNote(noteId);
        //todo 完成VO构造   note，type，tag，comment，user
        return null;
    }

    @ApiOperation("发布笔记")
    @PostMapping("/note")
    public void publishNote(@Valid @RequestBody Note note) throws Exception {
        User userInfo = jwtUtils.getUserInfo();
        note.setUser(userInfo);
        Type type = note.getType();
        note.setType(typeService.getType(type.getId()));
        Set<Tag> tags = note.getTags();
        note.setTags(tagService.getTags(tags));
        noteService.publishNote(note);
    }

}
