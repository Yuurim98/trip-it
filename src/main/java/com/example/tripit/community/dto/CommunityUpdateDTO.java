package com.example.tripit.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommunityUpdateDTO {

    private String postTitle;
    private String postContent;
    private String postPic;

}
