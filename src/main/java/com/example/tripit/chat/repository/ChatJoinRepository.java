package com.example.tripit.chat.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripit.chat.entity.ChatJoin;

public interface ChatJoinRepository extends JpaRepository<ChatJoin, Integer> {
}