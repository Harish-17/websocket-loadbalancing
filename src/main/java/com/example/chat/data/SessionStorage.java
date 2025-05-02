package com.example.chat.data;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStorage {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public Map<String, WebSocketSession> getSessions() {
        return sessions;
    }


    public void saveUserSession(String userId, WebSocketSession session) {
        this.sessions.put(userId, session);
    }

    public boolean hasUserSession(String toUser) {
        return this.sessions.containsKey(toUser);
    }

    public WebSocketSession getUserSession(String toUser) {
        return this.sessions.get(toUser);
    }

    public void removeUserSession(String userId) {
        try {
            this.sessions.get(userId).close();
            this.sessions.remove(userId);
        } catch (IOException e){
            System.out.println("Error in removing user " + e);
        }
    }
}
