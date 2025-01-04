package com.example.tripit.chat.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tripit.chat.entity.ChatMessage;
import com.example.tripit.chat.repository.ChatJoinRepository;
import com.example.tripit.chat.repository.ChatMessageRepository;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatJoinRepository chatJoinRepository;

    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        // chatMessage의 room_id가 chat_join에 있는지 확인
//        if (!chatJoinRepository.existsByRoomId(chatMessage.getRoomId())) {
//            throw new IllegalArgumentException("Invalid room_id: " + chatMessage.getRoomId());
//        }

        chatMessage.setSendTime(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }
}
