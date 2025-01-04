package com.example.tripit.error;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
    private List<CustomException.ErrorDetail> errors;
    private ErrorCode errorCode;

    public ErrorResponse(int status, String code, String message, List<CustomException.ErrorDetail> errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }


    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
