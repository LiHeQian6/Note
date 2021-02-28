package com.zhifou.note.message.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : li
 * @Date: 2021-02-28 16:27
 */
@Component
public class EventProducer {
    @Resource
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void fireMessageEvent(MessageEvent event) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        kafkaTemplate.send(event.getTopic(),mapper.writeValueAsString(event));
    }
}
