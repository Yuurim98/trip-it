package com.example.tripit.chat.websocket;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.tripit.chat.entity.ChatMessage;
import com.example.tripit.chat.service.ChatMessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    @Autowired
    private ChatMessageService chatMessageService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        JsonNode jsonNode = objectMapper.readTree(payload);
        String content = jsonNode.get("content").asText();
        // roomId를 가져오는 부분
        int roomId = Integer.parseInt(session.getAttributes().get("roomId").toString());

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setContent(content);
        chatMessage.setSendTime(LocalDateTime.now());

        chatMessageService.saveChatMessage(chatMessage);

        sendMessageToAll("/topic/public/" + roomId, chatMessage);
    }

    private void sendMessageToAll(String topic, ChatMessage chatMessage) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(chatMessage);
            for (WebSocketSession session : sessions.values()) {
                session.sendMessage(new TextMessage(jsonMessage));
            }
            System.out.println("Sending message to topic " + topic + ": " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }
}
