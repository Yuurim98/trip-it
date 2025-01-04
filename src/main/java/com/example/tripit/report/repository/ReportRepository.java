package com.example.tripit.report.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tripit.report.entity.ReportEntity;
import com.example.tripit.user.entity.UserEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {


    List<ReportEntity> findAll();
    
    List<ReportEntity> findAll(Sort sort);
    
	// UserEntity 객체를 기준으로 조회하는 메서드
	List<ReportEntity> findByUserId(UserEntity userId);
	
	// 특정 userId로 차단 목록 조회 및 정렬
	List<ReportEntity> findByUserId(UserEntity user, Sort sort);
	
	  // 정렬과 페이징이 적용된 쿼리 메서드
    Page<ReportEntity> findAll(Pageable pageable);
    
   
    @Query("SELECT r FROM ReportEntity r " +
            "WHERE r.reportDetail LIKE %:search% " +
            "OR r.userId.nickname LIKE %:search% " +
            "OR r.postId.postTitle LIKE %:search% " +
            "OR r.reportType.reportType LIKE %:search% " +
            "OR r.reportType.reportReason LIKE %:search%")
     Page<ReportEntity> findBySearchTerm(@Param("search") String search, Pageable pageable);
    
}
