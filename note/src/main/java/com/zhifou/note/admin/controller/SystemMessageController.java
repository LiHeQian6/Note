package com.zhifou.note.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.message.event.EventProducer;
import com.zhifou.note.message.event.MessageEvent;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : li
 * @Date: 2021-03-10 17:43
 */
@Controller
@RequestMapping("/admin")
public class SystemMessageController implements Constant {
    @Resource
    private EventProducer eventProducer;
    @Resource
    private UserDetailsServiceImp userService;

    @ApiOperation("发送系统通知")
    @PostMapping("/systemMessage")
    public void sendSystemMessage(@RequestParam String content, HttpServletResponse response) throws JsonProcessingException, UserException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);
        MessageEvent systemEvent = new MessageEvent();
        systemEvent.setUserId(user.getId());
        systemEvent.setTopic(TOPIC_SYSTEM);
        systemEvent.setExtra("content",content);
        eventProducer.fireMessageEvent(systemEvent);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
