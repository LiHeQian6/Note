package com.zhifou.note.message.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.message.event.EventProducer;
import com.zhifou.note.message.event.MessageEvent;
import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;

/**
 * @author : li
 * @Date: 2021-02-28 22:04
 */
@RestController
@Validated
public class LikeController implements Constant {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private LikeService likeService;
    @Resource
    private EventProducer eventProducer;


    @ApiOperation("点赞,再次发送取消")
    @PostMapping("/like")
    public void like(@ApiParam("note:1;comment:2;")int entityType, int entityId, int entityUserId) throws JsonProcessingException {
        Integer id = jwtUtils.getUserInfo().getId();
        likeService.like(id,entityType,entityId,entityUserId);
        boolean likeStatus = likeService.findEntityLikeStatus(id, entityType, entityId);
        if (likeStatus) {
            MessageEvent likeEvent = new MessageEvent();
            likeEvent.setUserId(entityUserId);
            likeEvent.setEntityType(entityType);
            likeEvent.setEntityId(entityId);
            likeEvent.setEntityUserId(id);
            likeEvent.setExtra("entityUserNickname",jwtUtils.getUserInfo().getNickName());
            likeEvent.setTopic(TOPIC_LIKE);
            eventProducer.fireMessageEvent(likeEvent);
        }
    }

    @ApiOperation("获取用户点赞记录")
    @GetMapping("/like")
    public Page<NoteVO> getLiked(@ApiParam("第几页") @Min(value = 0,message = "页数最小为0") int page,
                                 @ApiParam("页大小")@Min(value = 1,message = "页尺寸最小为1") int size) throws NoteException {
        User userInfo = jwtUtils.getUserInfo();
        return likeService.getUserLikedNotes(userInfo.getId(),page,size);
    }


}
