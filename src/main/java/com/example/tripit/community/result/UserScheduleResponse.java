package com.example.tripit.community.result;

import com.example.tripit.result.ResultCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserScheduleResponse {
    private long userId;
    private List<String> scheduleTitles;
    private List<String> scheduleId;
    private ResultCode resultCode;
    private int status;
    private String code;
    private String message;

    public UserScheduleResponse(long userId, List<String> scheduleTitles, List<String> scheduleId ,ResultCode resultCode) {
        this.userId = userId;
        this.scheduleTitles = scheduleTitles;
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.scheduleId = scheduleId;
    }

}
