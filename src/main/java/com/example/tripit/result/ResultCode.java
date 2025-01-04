package com.example.tripit.result;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // user
    REGISTER_SUCCESS(200, "M001", "회원가입성공."),
    LOGIN_SUCCESS(200, "M002", "token issued"),
    REISSUE_SUCCESS(200, "M003", "reissue success"),
    LOGOUT_SUCCESS(200, "M004", "로그아웃 완료"),
    GET_MY_INFO_SUCCESS(200, "M005", "내 정보 조회 완료"),
    ACCESS_TOKEN_EXPIRED(401, "1", "token expired"),
    LOGGOUT_REQUEST(400, "2", "logout"),

    //community
    SCHEDULETITLE_SUCCESS(200,"4", "schedule title success"),
    SCHEDULETITLE_NULL(200,"5", "schedule title null");

    //mypage


    private int status;
    private String code;
    private String message;

}
