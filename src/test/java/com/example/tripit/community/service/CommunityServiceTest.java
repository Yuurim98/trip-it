package com.example.tripit.community.service;

import com.example.tripit.community.entity.PostEntity;
import com.example.tripit.community.repository.PostRepository;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommunityServiceTest {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private PostRepository postRepository;

    private Long userId;
    private Long postId;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        //테스트용 데이터 생성

        userId = 7L;
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        PostEntity post = new PostEntity();
        post.setUserId(user);

        userId = post.getUserId().getUserId();
        postId = 32L;


    }

    @Test
    void deletePost() {

        // 포스트 삭제

        communityService.deletePost(userId, postId);

        // 포스트가 삭제되었는지 확인
        assertFalse(postRepository.existsById(postId), "Post should be deleted");
    }


}