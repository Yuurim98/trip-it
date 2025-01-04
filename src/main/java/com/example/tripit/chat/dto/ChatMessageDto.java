package com.example.tripit.chat.dto;

import java.time.LocalDateTime;

public class ChatMessageDto {

    private int messageId;
    private int userId;
    private int roomId;
    private String content;
    private LocalDateTime sendTime;

    // 기본 생성자
    public ChatMessageDto() {
    }

    // 생성자
    public ChatMessageDto(int userId, int roomId, String content, LocalDateTime sendTime) {
        this.userId = userId;
        this.roomId = roomId;
        this.content = content;
        this.sendTime = sendTime;
    }

    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }
}
