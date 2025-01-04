package com.example.tripit.result;

import lombok.Getter;

@Getter
public class ResultResponse {

    private int status;
    private String code;
    private String message;
    private String email;
    private String access;
    private String refresh;
    private long userId;
    private String role;

    public ResultResponse(ResultCode resultCode, String email, long userId, String role) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.email = email;
        this.userId = userId;
        this.role = role;
    }

    public ResultResponse(ResultCode resultCode, String email, String access, String refresh) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.email = email;
        this.access = access;
        this.refresh = refresh;
    }

    // User info response
    public static ResultResponse of(ResultCode resultCode, String email, long userId, String role) {
        return new ResultResponse(resultCode, email, userId, role);
    }

    // Token response
    public static ResultResponse of(ResultCode resultCode, String email, String access, String refresh, String role) {
        return new ResultResponse(resultCode, email, access, refresh);
    }

}
