package com.example.tripit.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "report_type")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" }) // Hibernate 프록시 관련 필드 무시
public class ReportTypeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_type") // 컬럼 이름을 정확히 맞춰야 함
	private String reportType;

	@Column(name = "report_reason", nullable = false)
	private String reportReason;
}
