package com.example.chat.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisPublisher {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ChannelTopic topic;

    public void publishMessage(String message) throws Exception {
        redisTemplate.convertAndSend(topic.getTopic(), message);

        System.out.println("Published to topic " + topic.getTopic());
    }
}
