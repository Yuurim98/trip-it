package com.example.tripit.community.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostDTO {
	private Long postId;
    private String postTitle;
    private String postContent;
    private Short personnel;
    private String postPic;
    private Boolean recruitStatus;
    private Integer viewCount;
    private Boolean exposureStatus;
    private Long userId;
    private Long scheduleId;
    
    private Long postedUserId;
    
    
}

