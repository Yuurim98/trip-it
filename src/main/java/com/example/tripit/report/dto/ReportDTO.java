package com.example.tripit.report.dto;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class ReportDTO {


    
    private Long reportId;
    private Long blockUserId;
    private Long postId;
    private String nickName;
    private String postTitle;
    private String reportType;
    private String reportReason;
    private String reportDetail;
    private int reportFalse;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd HH:mm:ss", timezone = "Asia/Seoul")

    private LocalDateTime reportDate;

    // Constructor
    public ReportDTO(Long reportId, Long blockUserId, Long postId, String nickName, String postTitle, 
                     String reportType, String reportReason, String reportDetail, 
                     int reportFalse, LocalDateTime reportDate) {
        this.reportId = reportId;
        this.blockUserId = blockUserId;
        this.postId = postId;
        this.nickName = nickName;
        this.postTitle = postTitle;
        this.reportType = reportType;
        this.reportReason = reportReason;
        this.reportDetail = reportDetail;
        this.reportFalse = reportFalse;
        this.reportDate = reportDate;
    }

	public boolean isBlockUser() {
		return true;
	}
    
    
}
