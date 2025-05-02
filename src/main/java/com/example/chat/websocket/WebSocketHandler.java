package com.example.chat.websocket;

import com.example.chat.data.SessionStorage;
import com.example.chat.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

@Component
class WebSocketHandler extends TextWebSocketHandler {

    private final SessionStorage sessionStorage;
    private final MessageHandler messageHandler;

    @Autowired
    public WebSocketHandler(SessionStorage sessionStorage, MessageHandler messageHandler) {
        this.sessionStorage = sessionStorage;
        this.messageHandler = messageHandler;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        sessionStorage.saveUserSession(userId, session);
        System.out.println("User connected: " + userId);
        session.sendMessage(new TextMessage("Connected to " + InetAddress.getLocalHost().getHostName()));
        session.sendMessage(new TextMessage("Welcome User " + userId));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            messageHandler.handleMessage(message.getPayload(), true);
            session.sendMessage(new TextMessage("Message Sent from " + InetAddress.getLocalHost().getHostName()));
        } catch (Exception e){
            session.sendMessage(new TextMessage("Error parsing message"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserId(session);
        sessionStorage.removeUserSession(userId);
        System.out.println("User disconnected: " + userId);
    }

    private String getUserId(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        if (query != null && query.contains("userId=")) {
            return query.split("userId=")[1];
        }
        return session.getId();
    }
}
