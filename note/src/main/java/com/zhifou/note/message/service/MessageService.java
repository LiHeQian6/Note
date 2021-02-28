package com.zhifou.note.message.service;

import com.zhifou.note.message.entity.Message;
import com.zhifou.note.message.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-02-08 17:26
 */
@Service
@Transactional
public class MessageService {
    @Resource
    private MessageRepository messageRepository;

    public void sendMessage(Message message) {
        messageRepository.save(message);
    }
}
