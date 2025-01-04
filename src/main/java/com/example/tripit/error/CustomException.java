package com.example.tripit.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomException extends RuntimeException {

    private int status;
    private String code;
    private String message;
    private List<ErrorDetail> errors;


    public CustomException(int status, String code, String message, List<ErrorDetail> errors) {
        super(message);
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errors = errors;
        //this.errors = new ArrayList<>(); // 기본값으로 빈 리스트를 설정
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
