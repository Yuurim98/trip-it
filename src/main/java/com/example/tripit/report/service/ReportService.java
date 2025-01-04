package com.example.tripit.report.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.tripit.block.service.BlockListService;
import com.example.tripit.community.entity.PostEntity;
import com.example.tripit.community.repository.PostRepository;
import com.example.tripit.report.dto.ReportDTO;
import com.example.tripit.report.entity.ReportEntity;
import com.example.tripit.report.entity.ReportTypeEntity;
import com.example.tripit.report.repository.ReportRepository;
import com.example.tripit.report.repository.ReportTypeRepository;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;

import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;

@Service
public class ReportService {

	private final ReportRepository reportRepository;
	private final UserRepository userRepository;
	private final ReportTypeRepository reportTypeRepository;
	private final BlockListService blockListService; // 차단 서비스 주입
	private final PostRepository postRepository; // 차단 서비스 주입

	public ReportService(ReportRepository reportRepository, UserRepository userRepository,
			ReportTypeRepository reportTypeRepository, BlockListService blockListService,
			PostRepository postRepository) {
		this.reportRepository = reportRepository;
		this.userRepository = userRepository;
		this.reportTypeRepository = reportTypeRepository;
		this.blockListService = blockListService;
		this.postRepository = postRepository;
	}

	// 신고 추가
	@Transactional
	public ReportEntity addReport(Long userId, Long postId, String reportType, String reportDetail, boolean blockUser) {
		// 유저 엔티티형식의 객체 생성
		UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// 신고 유형 찾기
		ReportTypeEntity reportTypeEntity = reportTypeRepository.findByReportType(reportType)
				.orElseThrow(() -> new RuntimeException("Report type not found"));

		// 포스트 존재 여부 확인
		PostEntity post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

		// 신고 엔티티 생성 및 설정
		ReportEntity report = new ReportEntity();
		report.setUserId(user);
		report.setPostId(post); // postId를 PostEntity 객체로 설정
		report.setReportType(reportTypeEntity);
		report.setReportDetail(reportDetail);
		report.setReportFalse(0); // 기본값 설정

		// 신고 저장
		ReportEntity savedReport = reportRepository.save(report);

		// 차단 처리
		if (blockUser = true) {
			blockListService.addBlock(userId, post.getUserId().getNickname()); // 신고 유형을 사용하여 차단 추가
		}

//		  // 신고 횟수 증가
//	    user.setReportCount(user.getReportCount() + 1);
//	    int reportThreshold = 3; // 예시 임계값
//	    if (user.getReportCount() >= reportThreshold) {
//	        Set<String> newRoles = new HashSet<>();
//	        newRoles.add("ROLE_A");
//	        user.setRoles(newRoles);
//	        // 새로운 역할로 업데이트된 사용자 엔티티 저장
//	        userRepository.save(user);
//	    }
		return savedReport;
	}

	// 관리자용 전체 조회(페이징 및 검색)
	   public Page<ReportDTO> getReports(String search, Pageable pageable, String sortKey, String sortValue) {
	        Page<ReportEntity> reportPage;
			if (search != null && !search.isEmpty()) {
	            reportPage = reportRepository.findBySearchTerm(search, pageable);
	        } else {
	            reportPage = reportRepository.findAll(pageable);
	        }

	        // ReportEntity를 ReportDTO로 변환
	        return reportPage.map(report -> new ReportDTO(
	            report.getReportId(),
	            report.getUserId().getUserId(),
	            report.getPostId().getPostId(),
	            report.getUserId().getNickname(),
	            report.getPostId().getPostTitle(),
	            report.getReportType().getReportType(),
	            report.getReportType().getReportReason(),
	            report.getReportDetail(),
	            report.getReportFalse(),
	            report.getReportDate()
	        ));
	    }


	// sort 특정 사용자의 신고 목록 조회 메서드
	public List<ReportDTO> reportForUser(Long userId) {
	    // 사용자 조회
	    UserEntity user = userRepository.findById(userId).orElse(null);

	    // 사용자가 없으면 빈 리스트 반환
	    if (user == null) {
	        return Collections.emptyList();
	    }

	    // 특정 사용자의 신고 목록 조회
	    List<ReportEntity> reports = reportRepository.findByUserId(user);

	    // ReportEntity를 ReportDTO로 변환
	    return reports.stream().map(report -> {
	        Long postId = report.getPostId().getPostId(); // PostEntity에서 postId 추출
	        String postTitle = report.getPostId().getPostTitle(); // PostEntity에서 postTitle 추출
	        Long blockUserId = report.getUserId().getUserId(); // userEntity에서 userId 추출
	        String nickName = report.getUserId().getNickname(); // userEntity에서 Nickname 추출
	        String reportType = report.getReportType().getReportType(); // ReportTypeEntity에서 ReportType 추출
	        String reportReason = report.getReportType().getReportReason(); // ReportTypeEntity에서 ReportReason 추출

	        return new ReportDTO(
	                report.getReportId(),
	                blockUserId,
	                postId,
	                nickName,
	                postTitle,
	                reportType,
	                reportReason,
	                report.getReportDetail(),
	                report.getReportFalse(),
	                report.getReportDate()
	        );
	    }).collect(Collectors.toList());
	}

	
	// 관리자용 신고 확인
	  @Transactional
	    public void updateReportFalse(Long reportId, int reportFalseValue) {
	        // ReportEntity 조회
	        ReportEntity report = reportRepository.findById(reportId)
	                .orElseThrow(() -> new RuntimeException("Report not found"));

	        // report_false 값 업데이트
	        report.setReportFalse(reportFalseValue);
	        reportRepository.save(report);

	        // report_false 값이 1이면 user의 report_count 값 증가
	        if (reportFalseValue == 1) {
	            UserEntity user = report.getPostId().getUserId(); // reportId로부터 userEntity를 가져옴
	            int newReportCount = user.getReportCount() + 1;
	            user.setReportCount(newReportCount);


	            // reportCount에 따라 역할 업데이트
	            if (newReportCount >= 7) {
	                user.setRole("ROLE_C");
	            } else if (newReportCount >= 5) {
	                user.setRole("ROLE_B");
	            } else if (newReportCount >= 3) {
	                user.setRole("ROLE_A");
	            }

	            userRepository.save(user);
	        }
	        
	    }
}
