package com.example.tripit.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommunityDTO {

    //User
    private long userId;
    private long loggedUserId; // 글쓴이와 로그인한 유저가 일치하는가를 확인하기 위함임
    private String nickname;
    private String gender;
    private String birth;
    private String userpic;

    //Schedule
    private long scheduleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String metroName;
    private String metroId;

    //Post
    private Long postId;
    private String postTitle;
    private String postContent;
    private Short personnel;
    private String postPic;
    private Boolean exposureStatus;
    private Integer viewCount;
    private LocalDateTime postDate;

    public CommunityDTO(Long postId, String postTitle, String postContent, Short personnel,
                        Integer viewCount, Boolean exposureStatus, String postPic, LocalDateTime postDate , long userId,
                        String nickname, String gender, String birth, String userpic, long scheduleId,String metroId,
                        String metroName, LocalDate startDate, LocalDate endDate) {

        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postPic = postPic;
        this.personnel = personnel;
        this.viewCount = viewCount;
        this.exposureStatus = exposureStatus;
        this.postDate = postDate;

        this.userId = userId;
        this.userpic = userpic;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;

        this.scheduleId = scheduleId;
        this.metroId = metroId;
        this.metroName = metroName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    //채팅쪽은 나중에 추가

}
