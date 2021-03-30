package com.zhifou.note.message.service;

import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.MessageVO;
import com.zhifou.note.message.entity.Message;
import com.zhifou.note.message.repository.MessageRepository;
import com.zhifou.note.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author : li
 * @Date: 2021-02-08 17:26
 */
@Service
@Transactional
public class MessageService implements Constant {
    @Resource
    private MessageRepository messageRepository;

    public void sendMessage(Message message) {
        messageRepository.save(message);
    }

    /**
     * @param: user
     * @return java.util.HashMap<java.lang.String,java.lang.Integer>
     * @description 获取用户所有类型消息未读的数量
     * @author li
     * @Date 2021/3/25 21:32
     */
    public HashMap<String, Integer> getAllNewMessageNum(User user) {
        HashMap<String, Integer> map = new HashMap<>();
        int ms_like = messageRepository.countByToAndMsTypeAndCreateTimeAfter(user,TOPIC_LIKE,user.getLastReadTime());
        int ms_collect = messageRepository.countByToAndMsTypeAndCreateTimeAfter(user,TOPIC_COLLECT,user.getLastReadTime());
        int ms_comment = messageRepository.countByToAndMsTypeAndCreateTimeAfter(user,TOPIC_COMMENT,user.getLastReadTime());
        int ms_follow = messageRepository.countByToAndMsTypeAndCreateTimeAfter(user,TOPIC_FOLLOW,user.getLastReadTime());
        int ms_system = messageRepository.countByToAndMsTypeAndCreateTimeAfter(user,TOPIC_SYSTEM,user.getLastReadTime());
        map.put(TOPIC_LIKE,ms_like);
        map.put(TOPIC_COLLECT,ms_collect);
        map.put(TOPIC_COMMENT,ms_comment);
        map.put(TOPIC_FOLLOW,ms_follow);
        map.put(TOPIC_SYSTEM,ms_system);
        return map;
    }
    /**
     * @param: user
     * @return java.util.HashMap<java.lang.String,java.lang.Integer>
     * @description 获取用户未读消息总数量
     * @author li
     * @Date 2021/3/25 21:32
     */
    public int getNewMessageNum(User user) {
        return messageRepository.countByToAndCreateTimeAfter(user,user.getLastReadTime());
    }


    /**
     * @param: userInfo
     * @param: type
     * @return java.util.List<com.zhifou.note.bean.MessageVO>
     * @description 分页获取指定类型的消息，按时间倒序
     * @author li
     * @Date 2021/3/28 10:58
     */
    public Page<MessageVO> getNewMessageNumByType(User userInfo, String type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Message> messages = messageRepository.findByToAndMsTypeAndCreateTimeAfterOrderByCreateTimeDesc(userInfo, type, userInfo.getLastReadTime(), pageRequest);
        ArrayList<MessageVO> messageVOS = new ArrayList<>();
        for (Message message : messages.getContent()) {
            MessageVO messageVO = new MessageVO(message);
            messageVOS.add(messageVO);
        }
        return new PageImpl<>(messageVOS,pageRequest,messageVOS.size());
    }
}
