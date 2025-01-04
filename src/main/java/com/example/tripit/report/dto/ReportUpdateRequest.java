package com.example.tripit.report.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class ReportUpdateRequest {
    private Long reportId;
    private int reportFalse; // 요청 데이터에 맞게 'reportFalse'로 수정


    // 파라미터가 있는 생성자
    public ReportUpdateRequest(Long reportId, int reportFalse) {
        this.reportId = reportId;
        this.reportFalse = reportFalse;
    }

    // Getter 및 Setter
    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public int getReportFalse() {
        return reportFalse;
    }

    public void setReportFalse(int reportFalse) {
        this.reportFalse = reportFalse;
    }
}