package com.example.tripit.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomErrorResponse extends RuntimeException {

    private int status;
    private String code;
    private String message;
    private List<ErrorDetail> errors;
    private ErrorCode errorCode;


    public CustomErrorResponse(int status, String code, String message, List<ErrorDetail> errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }
    public CustomErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }





    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ErrorDetail {
        private String field;
        private String value;
        private String reason;

        public ErrorDetail(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}
