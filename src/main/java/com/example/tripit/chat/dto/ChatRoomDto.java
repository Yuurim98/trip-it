package com.example.tripit.chat.dto;
public class ChatRoomDto {
    private String chatroomName;
    private char chatroomType;
    private int postId;

    // getters and setters
    public String getChatroomName() {
        return chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public char getChatroomType() {
        return chatroomType;
    }

    public void setChatroomType(char chatroomType) {
        this.chatroomType = chatroomType;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
