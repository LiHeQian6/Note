package com.zhifou.note.message.controller;

import com.zhifou.note.message.service.FollowService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : li
 * @Date: 2021-02-28 22:05
 */
@RestController
public class FollowController {
    @Resource
    private FollowService followService;
    @Resource
    private JwtUtils jwtUtils;

    @ApiOperation("关注某人")
    @PostMapping("/follow")
    public void follow(int targetId){
        User userInfo = jwtUtils.getUserInfo();
        followService.follow(userInfo.getId(),targetId);
    }

    @ApiOperation("取消对某人的关注")
    @DeleteMapping("/follow")
    public void unfollow(int targetId){
        User userInfo = jwtUtils.getUserInfo();
        followService.unfollow(userInfo.getId(),targetId);
    }

    @ApiOperation("获取自己的关注列表")
    @GetMapping("/followee")
    public List<Map<String, Object>> getFollowee(@ApiParam("从第x(first:0)个开始")@RequestParam int offset,
                                                 @ApiParam("每页有几个") @RequestParam int limit){
        User userInfo = jwtUtils.getUserInfo();
        return followService.getFollowee(userInfo.getId(),offset,limit);
    }
    @ApiOperation("获取自己的粉丝列表")
    @GetMapping("/follower")
    public List<Map<String, Object>> getFollowers(@ApiParam("从第x(first:0)个开始")@RequestParam int offset,
                                                  @ApiParam("每页有几个")@RequestParam int limit){
        User userInfo = jwtUtils.getUserInfo();
        return followService.getFollowers(userInfo.getId(),offset,limit);
    }

}
