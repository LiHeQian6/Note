package com.zhifou.note.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.exception.MessageException;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.message.entity.Message;
import com.zhifou.note.message.event.EventProducer;
import com.zhifou.note.message.event.MessageEvent;
import com.zhifou.note.message.service.MessageService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author : li
 * @Date: 2021-03-10 17:43
 */
@RestController
@RequestMapping("/admin")
public class SystemMessageController implements Constant {
    @Resource
    private EventProducer eventProducer;
    @Resource
    private UserDetailsServiceImp userService;
    @Resource
    private MessageService messageService;

    @ApiOperation("发送系统通知")
    @PostMapping("/system-message")
    public void sendSystemMessage(@RequestParam @NotBlank String content, HttpServletResponse response) throws JsonProcessingException, UserException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);
        MessageEvent systemEvent = new MessageEvent();
        systemEvent.setUserId(user.getId());
        systemEvent.setTopic(TOPIC_SYSTEM);
        systemEvent.setExtra("content",content);
        eventProducer.fireMessageEvent(systemEvent);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @ApiOperation("分页获取所有系统消息")
    @GetMapping("/system-messages")
    public DataTablesOutput<Message> getMessages(@Valid DataTablesInput input){
        input.getColumns().remove(input.getColumns().size()-1);
        return messageService.getSystemMessages(input);
    }

    @ApiOperation("删除系统消息")
    @DeleteMapping("/system-message/{id}")
    public String deleteNote(@PathVariable int id) {
        try {
            messageService.deleteSystemMessage(id);
        } catch (MessageException e) {
            return e.getMessage();
        }
        return "true";
    }

}
