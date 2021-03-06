package com.zhifou.note.note.controller;

import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.message.service.CollectService;
import com.zhifou.note.message.service.FollowService;
import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.message.service.LookService;
import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.service.CommentService;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-02-08 17:23
 */
@Api("笔记模块")
@RestController
public class NoteController implements Constant {
    @Resource
    private NoteService noteService;
    @Resource
    private LookService lookService;
    @Resource
    private LikeService likeService;
    @Resource
    private CommentService commentService;
    @Resource
    private FollowService followService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private CollectService collectService;

    @ApiOperation("浏览笔记")
    @GetMapping("/note/{id}")
    public NoteVO browseNote(@PathVariable int id, HttpServletRequest request) throws NoteException {
        lookService.look(request.getRemoteAddr(),id);
        long look = lookService.lookNum(id);
        long like = likeService.findEntityLikeCount(ENTITY_TYPE_NOTE, id);
        long collect = collectService.getNoteCollectCount(id);
        Set<Comment> comments = commentService.getNoteComments(id);
        Note note = noteService.getNote(id);
        NoteVO noteVO = new NoteVO(note, like, look, collect, comments);
        User userInfo = jwtUtils.getUserInfo();
        if (userInfo !=null) {
            boolean followed = followService.hasFollowed(userInfo.getId(), note.getUser().getId());
            noteVO.getUser().setFollow(followed);
            noteVO.setIsLike(likeService.findEntityLikeStatus(userInfo.getId(),ENTITY_TYPE_NOTE, id));
            noteVO.setIsCollect(collectService.hasCollected(userInfo.getId(),id));
        }
        return noteVO;
    }

    @ApiOperation("发布笔记")
    @PostMapping("/note")
    public void publishNote(@ApiParam("只需要传title,content,type.id,tags=[tag.id]") @Valid @RequestBody Note note) throws Exception {
        User userInfo = jwtUtils.getUserInfo();
        note.setUser(userInfo);
        noteService.addNote(note);
    }

    @ApiOperation("修改笔记")
    @PutMapping("/note")
    public void editNote(@ApiParam("只需要传title,content,type.id,tags=[tag.id]") @Valid @RequestBody Note newNote) throws NoteException {
        User userInfo = jwtUtils.getUserInfo();
        Note note = noteService.getNote(newNote.getId(),userInfo.getUsername());
        if (note.update(newNote)) {
            noteService.updateNote(note);
        }
    }

    @ApiOperation("删除笔记")
    @DeleteMapping("/note/{id}")
    public void deleteNote(@PathVariable int id) throws NoteException {
        User userInfo = jwtUtils.getUserInfo();
        noteService.deleteNote(id,userInfo.getUsername());
    }

}
