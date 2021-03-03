package com.zhifou.note.message.controller;

import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-02-28 22:04
 */
@RestController
public class LikeController {    //todo 点赞，关注，评论，浏览，收藏，系统通知，/*私信*/，类别，标签，评论返回，笔记增删改查，类别，标签增删改查

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private LikeService likeService;


    @ApiOperation("点赞,再次发送取消")
    @PostMapping("/like")
    public void like(@ApiParam("note:1;comment:2;")int entityType, int entityId, int entityUserId){
        Integer id = jwtUtils.getUserInfo().getId();
        likeService.like(id,entityType,entityId,entityUserId);
    }
}
