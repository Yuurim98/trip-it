package com.example.tripit.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.tripit.chat.entity.ChatMessage;
import com.example.tripit.chat.entity.ChatRoom;
import com.example.tripit.chat.service.ChatJoinService;
import com.example.tripit.chat.service.ChatMessageService;
import com.example.tripit.chat.service.ChatRoomService;
import com.example.tripit.user.repository.UserRepository;
@Controller
@RequestMapping("/chat")
@RestController
//http://localhost:8080/chat/createChatRoom
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatRoomService chatRoomService;
    
    @Autowired
    private ChatJoinService chatJoinService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    public ChatController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }
    
    // HTTP GET 방식으로 채팅방 생성화면 보여주기
    @GetMapping("/createChatRoom")
    public ModelAndView createChatRoom() {
        ModelAndView mav = new ModelAndView();
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
        // 만들어진 채팅방 확인
        mav.addObject("chatRooms", chatRooms);
        System.out.println("Chat Rooms: " + chatRooms); // 콘솔에서 확인
        mav.setViewName("chat/createChatRoom");
        return mav;
    }
    
    // 현재 만들어진 채팅방 리스트 호출
    @GetMapping("/chatRooms")
    @ResponseBody // JSON 형식으로 반환하기 위한 어노테이션
    public List<ChatRoom> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
//        System.out.println("Chat Rooms: " + chatRooms); // 콘솔에서 확인
        return chatRooms;
    }

    

    // WebSocket을 이용한 메시지 송신 처리
    @MessageMapping("/send/message")
    @SendTo("/topic/public")
    public ChatMessage broadcastMessage(ChatMessage message) {
        return chatMessageService.saveChatMessage(message);
    }

    // AJAX를 이용한 메시지 송신 처리
    @PostMapping("/ajax/send")
    public void sendMessageViaAjax(@RequestBody ChatMessage message) {
        ChatMessage savedMessage = chatMessageService.saveChatMessage(message);
        messagingTemplate.convertAndSend("/topic/public", savedMessage);
    }
    
}
