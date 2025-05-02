package com.example.chat.message;

import com.example.chat.data.SessionStorage;
import com.example.chat.parser.JsonParser;
import com.example.chat.redis.RedisPublisher;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.InetAddress;

@Component
public class MessageHandler {

    private final SessionStorage sessionStorage;
    private final JsonParser jsonParser;
    private final RedisPublisher redisPublisher;

    @Autowired
    public MessageHandler(JsonParser jsonParser, SessionStorage sessionStorage, RedisPublisher redisPublisher) {
        this.jsonParser = jsonParser;
        this.sessionStorage = sessionStorage;
        this.redisPublisher = redisPublisher;
    }

    public void handleMessage(String message, boolean routeToRedis) throws Exception {
        try {
            JsonNode json = jsonParser.getJSON(message);
            String toUser = json.get("to").asText();

            if (sessionStorage.hasUserSession(toUser)) {
                WebSocketSession targetSession = sessionStorage.getUserSession(toUser);
                if (targetSession != null && targetSession.isOpen()) {
                    targetSession.sendMessage(new TextMessage(message + " " +
                            InetAddress.getLocalHost().getHostName()));
                }
            } else if(routeToRedis){
                // Publish to Redis so other instances who can send it to this user can pick it up
                redisPublisher.publishMessage(message);
            }
        } catch (Exception e){
            System.out.println(e.getStackTrace());
            throw e;
        }
    }
}
