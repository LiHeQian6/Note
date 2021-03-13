package com.zhifou.note.message.controller;

import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author : li
 * @Date: 2021-02-28 22:04
 */
@RestController
public class LikeController {    //todo 系统通知，/*私信*/，类别，标签删改(管理模块实现)

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

    @ApiOperation("获取用户点赞记录")
    @GetMapping("/like")
    public Set<NoteVO> getLiked(int offset,int limit) throws NoteException {
        User userInfo = jwtUtils.getUserInfo();
        return likeService.getUserLikedNote(userInfo.getId(),offset,limit);
    }


}
