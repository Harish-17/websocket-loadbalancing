package com.example.chat.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonParser {

    @Autowired
    ObjectMapper objectMapper;

    public JsonNode getJSON(String textMessage) throws JsonProcessingException {
        JsonNode json = objectMapper.readTree(textMessage);
        return json;
    }

    public String getJSONString(JsonNode jsonNode) throws JsonProcessingException {
        return objectMapper.writeValueAsString(jsonNode);
    }
}
