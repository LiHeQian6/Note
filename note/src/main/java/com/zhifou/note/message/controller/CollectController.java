package com.zhifou.note.message.controller;

import com.zhifou.note.exception.bean.NoteException;
import com.zhifou.note.message.service.CollectService;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : li
 * @Date: 2021-02-28 22:05
 */
@RestController
public class CollectController {
    @Resource
    private CollectService collectService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private NoteService noteService;

    @ApiOperation("收藏笔记，再次发送取消")
    @PostMapping("/collect")
    public void collect(int id){
        User userInfo = jwtUtils.getUserInfo();
        collectService.collectNote(userInfo.getId(),id);
    }

    @ApiOperation("获取自己的收藏列表")
    @GetMapping("/collect")
    public List<Map<String, Object>> getCollects(@ApiParam("从第x(first:0)个开始") int offset, @ApiParam("每页有几个") int limit) throws NoteException {
        User userInfo = jwtUtils.getUserInfo();
        return collectService.getCollects(userInfo.getId(),offset,limit);
    }
    @ApiOperation("获取收藏该笔记的用户列表")
    @GetMapping("/collector/{noteId}")
    public List<Map<String, Object>> getFollowers(@ApiParam("从第x(first:0)个开始") int offset, @ApiParam("每页有几个") int limit, @PathVariable int noteId) throws NoteException {
        User userInfo = jwtUtils.getUserInfo();
        if (noteService.getNote(noteId,userInfo.getUsername())!=null) {//todo 测试
            return collectService.getNoteCollect(noteId,offset,limit);
        }
        return null;
    }
}
