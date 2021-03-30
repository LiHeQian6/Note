package com.zhifou.note.message.controller;

import com.zhifou.note.bean.MessageVO;
import com.zhifou.note.message.service.MessageService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.util.HashMap;

/**
 * @author : li
 * @Date: 2021-03-25 17:45
 */
@RestController
public class MessageController {
    @Resource
    private MessageService messageService;
    @Resource
    private JwtUtils jwtUtils;

    @ApiOperation("/获取每种消息的未读个数")
    @GetMapping("/message/types-not-read-num")
    public HashMap<String,Integer> getNewMessageNumByType(){
        User userInfo = jwtUtils.getUserInfo();
        return messageService.getAllNewMessageNum(userInfo);
    }

    @ApiOperation("/获取消息的未读总数")
    @GetMapping("/message/not-read-num")
    public int getNewMessageNum(){
        User userInfo = jwtUtils.getUserInfo();
        return messageService.getNewMessageNum(userInfo);
    }

    @ApiOperation("按类型获取消息")
    @GetMapping("/message/{type}")
    public Page<MessageVO> getMessage(@PathVariable String type,
                                      @RequestParam @ApiParam("第几页") @Min(value = 0, message = "页数最小为0") int page,
                                      @RequestParam @ApiParam("页大小") @Min(value = 1, message = "页尺寸最小为1") int size){
        User userInfo = jwtUtils.getUserInfo();
        return messageService.getNewMessageNumByType(userInfo,type,page,size);
    }


}
