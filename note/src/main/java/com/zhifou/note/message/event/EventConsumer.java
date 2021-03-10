package com.zhifou.note.message.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.exception.CommentException;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.message.entity.Message;
import com.zhifou.note.message.service.MessageService;
import com.zhifou.note.note.entity.Comment;
import com.zhifou.note.note.service.CommentService;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.user.service.UserDetailsServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : li
 * @Date: 2021-02-28 16:38
 */
@Slf4j
@Component
public class EventConsumer implements Constant {
    @Resource
    private MessageService messageService;
    @Resource
    private UserDetailsServiceImp userDetailsServiceImp;
    @Resource
    private NoteService noteService;
    @Resource
    private CommentService commentService;

    /**
     * @param: record
     * @return void
     * @description 系统通知
     * @author li
     * @Date 2021/2/28 21:05
     */
    @KafkaListener(topics = {TOPIC_LIKE,TOPIC_COLLECT,TOPIC_COMMENT,TOPIC_FOLLOW})
    public void messageListen(ConsumerRecord record) throws NoteException, CommentException {
        if (record==null) {
            log.error("消息内容为空！");
            return;
        }
        MessageEvent event;
        ObjectMapper mapper = new ObjectMapper();
        try {
            event = mapper.reader().readValue(record.value().toString(), MessageEvent.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }
        if (event==null) {
            log.error("消息格式错误！");
            return;
        }
        Message message = new Message();
        message.setFrom(userDetailsServiceImp.loadUserById(SYSTEM_USER_ID));
        message.setTo(userDetailsServiceImp.loadUserById(event.getEntityUserId()));
        message.setCreateTime(new Date());
        message.setMsType(event.getTopic());
        HashMap<String, Object> content = new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("userNickName",userDetailsServiceImp.loadUserById(event.getUserId()).getNickName());
        content.put("entityId",event.getEntityId());
        content.put("entityType",event.getEntityType());
        if (event.getEntityType()==ENTITY_TYPE_NOTE){
            content.put("entityInfo",noteService.getNote(event.getEntityId()).getTitle());
        }else if (event.getEntityType()==ENTITY_TYPE_COMMENT){
            Comment comment = commentService.getComment(event.getEntityId());
            content.put("entityInfo", comment.getContent()
                    .substring(0, Math.min(comment.getContent().length(), 20)));
        }
        if (!event.getExtra().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getExtra().entrySet()) {
                content.put(entry.getKey(),entry.getValue());
            }
        }
        try {
            message.setContent(mapper.writeValueAsString(content));
        } catch (JsonProcessingException e) {
            log.error("message.content JSON解析异常");
            return;
        }
        messageService.sendMessage(message);
    }

}
