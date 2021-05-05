package com.zhifou.note.message.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.message.event.EventProducer;
import com.zhifou.note.message.event.MessageEvent;
import com.zhifou.note.message.service.FollowService;
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
public class FollowController implements Constant {
    @Resource
    private FollowService followService;
    @Resource
    private EventProducer eventProducer;
    @Resource
    private JwtUtils jwtUtils;

    @ApiOperation("关注某人")
    @PostMapping("/follow")
    public void follow(int targetId) throws JsonProcessingException {
        User userInfo = jwtUtils.getUserInfo();
        if (!followService.hasFollowed(userInfo.getId(),targetId)) {
            followService.follow(userInfo.getId(),targetId);
            MessageEvent followEvent = new MessageEvent();
            followEvent.setEntityType(ENTITY_TYPE_USER);
            followEvent.setEntityId(targetId);
            followEvent.setUserId(targetId);
            followEvent.setEntityUserId(userInfo.getId());
            followEvent.setExtra("entityUserNickname",userInfo.getNickName());
            followEvent.setTopic(TOPIC_FOLLOW);
            eventProducer.fireMessageEvent(followEvent);
        }

    }

    @ApiOperation("取消对某人的关注")
    @DeleteMapping("/follow")
    public void unfollow(int targetId){
        User userInfo = jwtUtils.getUserInfo();
        followService.unfollow(userInfo.getId(),targetId);
    }

    @ApiOperation("获取自己的粉丝列表")
    @GetMapping("/followee")
    public List<Map<String, Object>> getFollowee(@ApiParam("第几页") @Min(value = 0,message = "页数最小为0") int page,
                                                 @ApiParam("页大小")@Min(value = 1,message = "页尺寸最小为1") int size) throws UserException {
        int offset=page*size;
        User userInfo = jwtUtils.getUserInfo();
        return followService.getFollowees(userInfo.getId(),offset,size);
    }
    @ApiOperation("获取自己的关注列表")
    @GetMapping("/follower")
    public List<Map<String, Object>> getFollowers(@ApiParam("第几页") @Min(value = 0,message = "页数最小为0") int page,
                                                  @ApiParam("页大小")@Min(value = 1,message = "页尺寸最小为1") int size) throws UserException {
        int offset=page*size;
        User userInfo = jwtUtils.getUserInfo();
        return followService.getFollowers(userInfo.getId(),offset,size);
    }

}
