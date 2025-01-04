package com.example.tripit.chat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tripit.chat.entity.ChatRoom;
import com.example.tripit.chat.repository.ChatRoomRepository;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    
    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }
    
    
//    // 채팅방 생성 
//    public ChatRoom createChatRoom(String chatroomName, char chatroomType, int postId) {
//        ChatRoom chatRoom = new ChatRoom(postId, chatroomName, chatroomType);
//        return chatRoomRepository.save(chatRoom);
//    }
    
    public ChatRoom createChatRoom(String chatroomName, int postId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatroomName(chatroomName);
        chatRoom.setPostId(postId);
        chatRoom.setCreatedTime(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }
    
    // 현재 만들어진 채팅방 리스트 호출
    public List<ChatRoom> getAllChatRooms() {
        System.out.println(chatRoomRepository.findAll());
    	return chatRoomRepository.findAll();
        
    }

    //현재 채팅방이 존재하는지 확인
    public ChatRoom findByPostId(int postId) {
        return chatRoomRepository.findByPostId(postId);
    }

}
