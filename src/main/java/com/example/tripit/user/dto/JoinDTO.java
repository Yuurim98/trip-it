package com.example.tripit.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinDTO {

    private String email;

    private String username;

    private String nickname;

    private String password;

    private String birth;

    private String gender; // 남자: 0, 여자: 1


}
