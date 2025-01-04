package com.example.tripit.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tripit.chat.entity.ChatJoin;
import com.example.tripit.chat.repository.ChatJoinRepository;


@Service
public class ChatJoinService {

    @Autowired
    private ChatJoinRepository chatJoinRepository;

    public void joinRoom(int userId, int roomId) {
        ChatJoin chatJoin = new ChatJoin();
        chatJoin.setUserId(userId);
        chatJoin.setRoomId(roomId);
        chatJoinRepository.save(chatJoin);
    }
}