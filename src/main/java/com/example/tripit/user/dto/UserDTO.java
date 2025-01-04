package com.example.tripit.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.xml.crypto.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String email;
    private String username;
    private String nickname;
    private String password;
    private String birth;
    private String gender;

    private String intro;

    private String role;

    private LocalDate regdate;

    private String userpic;
    private String socialType;

    private int reportCount;
    private LocalDate endDate;


}
