package com.example.tripit.report.entity;

import java.time.LocalDateTime;

import com.example.tripit.community.entity.PostEntity;
import com.example.tripit.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "report")
public class ReportEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Long reportId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_type", referencedColumnName = "report_type")
	private ReportTypeEntity reportType;

	@Column(name = "report_detail", length = 50, nullable = false, columnDefinition = "VARCHAR(50) DEFAULT ''")
	private String reportDetail = ""; // 기본값으로 초기화

	@Column(name = "report_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime reportDate = LocalDateTime.now();

	@Column(name = "report_false", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private int reportFalse;

	// Getters and Setters
	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public UserEntity getUserId() {
		return userId;
	}

	public void setUserId(UserEntity userId) {
		this.userId = userId;
	}

	public PostEntity getPostId() {
		return postId;
	}

	public void setPostId(PostEntity postId) {
		this.postId = postId;
	}

	public String getReportDetail() {
		return reportDetail;
	}

	public void setReportDetail(String reportDetail) {
		this.reportDetail = reportDetail;
	}

	public LocalDateTime getReportDate() {
		return reportDate;
	}

	public void setReportDate(LocalDateTime reportDate) {
		this.reportDate = reportDate;
	}

	public int getReportFalse() {
		return reportFalse;
	}

	public void setReportFalse(int reportFalse) {
		this.reportFalse = reportFalse;
	}

	public ReportTypeEntity getReportType() {
		return reportType;
	}

	public void setReportType(ReportTypeEntity reportType) {
		this.reportType = reportType;
	}
}
