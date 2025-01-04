package com.example.tripit.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(500, "C001", "internal server error"),
    INVALID_INPUT_VALUE(400, "C002", "invalid input type"),
    METHOD_NOT_ALLOWED(405, "C003", "method not allowed"),
    INVALID_TYPE_VALUE(400, "C004", "invalid type value"),
    BAD_CREDENTIALS(400, "C005", "bad credentials"),

    // Member
    USER_EMAIL_ALREADY_EXISTS(400, "1", "user email already exists"),
    USER_NICKNAME_ALREADY_EXISTS(400, "2", "user email already exists"),
    USER_NOT_EXISTS(400, "2", "user not exists"),
    NO_AUTHORITY(403, "M003", "no authority"),
    NEED_LOGIN(401, "M004", "need login"),
    AUTHENTICATION_NOT_FOUND(401, "M005", "Security Context에 인증 정보가 없습니다."),
    MEMBER_ALREADY_LOGOUT(400, "M006", "member already logout"),

    // Auth
    REFRESH_TOKEN_INVALID(400, "A001", "refresh token invalid"),
    ACCESS_TOKEN_EXPIRED(401, "1", "token expired"),

    LOGOUT_REQUEST(400, "2", "logout request"),
    ACCESS_TOKEN_INVALID(400, "A002", "access token invalid"),
    
    //Chat
    MESSAGE_NULL(400,"1","message is null"),

    //Schedule
    SCHEDULE_NOT_FOUND(404, "404", "Schedule not found"),
    FAIL_SAVE_SCHEDULE(500, "500", "schedule save fails"),
    NO_CONTENT(204, "N001", "No content"),

    //MyPage
    DUPLICATE_NICKNAMES(409, "409", "Duplicate nicknames"),
    USER_NOT_FOUND(404, "404", "User not found"),
    POST_NOT_FOUND(404, "404", "Post not found"),
    INTRO_TOO_LONG(422, "422", "Intro too long"),
    //NO_CONTENT(204, "N001", "No content"),

    //MyPage


    SEARCH_EXISTS(200, "1", "검색 결과가 없습니다."),
	//block
	BLOCK_EXISTS(500, "1", "자기 자신을 차단할 수 없습니다."),
	BLOCK_EXISTS2(500, "2", "이미 차단한 사용자 입니다."),
	BLOCK_EXISTS3(500, "3", "차단한 대상이 아닙니다");


    //Community
    private int status;
    private final String code;
    private final String message;
    

}
