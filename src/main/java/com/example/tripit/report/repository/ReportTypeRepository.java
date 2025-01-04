package com.example.tripit.report.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripit.report.entity.ReportTypeEntity;

public interface ReportTypeRepository extends JpaRepository<ReportTypeEntity, String> {

	Optional<ReportTypeEntity> findByReportType(String reportType);

	
}
