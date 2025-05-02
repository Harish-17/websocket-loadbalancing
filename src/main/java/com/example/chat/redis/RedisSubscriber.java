package com.example.chat.redis;

import com.example.chat.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RedisSubscriber implements MessageListener {

    private final MessageHandler messageHandler;

    @Autowired
    public RedisSubscriber(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String payload = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received message from topic " + message);
            messageHandler.handleMessage(payload, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
