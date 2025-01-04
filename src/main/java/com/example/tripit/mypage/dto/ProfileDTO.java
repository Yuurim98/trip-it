package com.example.tripit.mypage.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

    private Long userId;
    private String email;
    private String username;
    private String nickname;
    private String birth;
    private String gender;
    private String intro;
    private String userpic;
    private String role;
    private LocalDate regdate;
    private String social_type;
    //private int reportCount;
    private LocalDate endDate;


}
