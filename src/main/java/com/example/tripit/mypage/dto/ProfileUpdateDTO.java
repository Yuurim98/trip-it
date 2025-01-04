package com.example.tripit.mypage.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateDTO {


    private String nickname;
    private String intro;
    private String userpic;


}
