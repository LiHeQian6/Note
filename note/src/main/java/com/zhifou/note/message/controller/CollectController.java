package com.zhifou.note.message.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.message.event.EventProducer;
import com.zhifou.note.message.event.MessageEvent;
import com.zhifou.note.message.service.CollectService;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/**
 * @author : li
 * @Date: 2021-02-28 22:05
 */
@RestController
@Validated
public class CollectController implements Constant {
    @Resource
    private CollectService collectService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private NoteService noteService;
    @Resource
    private EventProducer eventProducer;

    @ApiOperation("收藏笔记，再次发送取消")
    @PostMapping("/collect")
    public void collect(int id) throws JsonProcessingException, NoteException {
        User userInfo = jwtUtils.getUserInfo();
        collectService.collectNote(userInfo.getId(),id);
        MessageEvent collectEvent = new MessageEvent();
        collectEvent.setUserId(userInfo.getId());
        collectEvent.setEntityType(ENTITY_TYPE_NOTE);
        collectEvent.setEntityId(id);
        User targetUser = noteService.getNote(id).getUser();
        collectEvent.setEntityUserId(targetUser.getId());
        collectEvent.setTopic(TOPIC_COLLECT);
        eventProducer.fireMessageEvent(collectEvent);
    }

    @ApiOperation("获取自己的收藏列表")
    @GetMapping("/collect")
    public List<Map<String, Object>> getCollects(@ApiParam("第几页") @Min(value = 0,message = "页数最小为0") int page,
                                                 @ApiParam("页大小")@Min(value = 1,message = "页尺寸最小为1") int size) throws NoteException {
        User userInfo = jwtUtils.getUserInfo();
        return collectService.getCollects(userInfo.getId(),page,size);
    }
    @ApiOperation("获取收藏该笔记的用户列表")
    @GetMapping("/collector/{noteId}")
    public List<Map<String, Object>> getFollowers(@ApiParam("第几页") @Min(value = 0,message = "页数最小为0") int page,
                                                  @ApiParam("页大小")@Min(value = 1,message = "页尺寸最小为1") int size,
                                                  @PathVariable int noteId) throws NoteException, UserException {
        User userInfo = jwtUtils.getUserInfo();
        if (noteService.getNote(noteId,userInfo.getUsername())!=null) {
            return collectService.getNoteCollect(noteId,page,size);
        }
        return null;
    }
}
