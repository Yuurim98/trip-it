package com.example.tripit.report.contriller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripit.report.dto.ReportDTO;
import com.example.tripit.report.dto.ReportUpdateRequest;
import com.example.tripit.report.entity.ReportEntity;
import com.example.tripit.report.repository.ReportTypeRepository;
import com.example.tripit.report.service.ReportService;
import com.example.tripit.user.dto.CustomUserDetails;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;

@RestController
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportTypeRepository reportTypeRepository;

	// 신고 추가
	@PostMapping("/add")
	public ResponseEntity<List<ReportEntity>> addReport(@RequestBody ReportDTO reportRequest,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		// 유저 정보 시큐리티 확인
		String email = customUserDetails.getUsername();
		Long userId = userRepository.findUserIdByEmail(email);
		Optional<UserEntity> entity = userRepository.findById(userId);

		if (entity.isEmpty()) {
			return ResponseEntity.badRequest().build(); // 유저가 없으면 bad request 반환
		}

		// 신고 추가 및 차단 처리
		ReportEntity report = reportService.addReport(userId, reportRequest.getPostId(), reportRequest.getReportType(),
				reportRequest.getReportDetail(), reportRequest.isBlockUser());

		// 업데이트된 신고 리스트 가져오기 및 정렬 추가
		List<ReportEntity> updatedReport = reportService.findAll("reportDate", "desc");

		// 업데이트된 신고 리스트 리턴
		return ResponseEntity.ok(updatedReport);
	}

//	// 모든 신고대상자확인
//	@GetMapping("/all")
//	public ResponseEntity<List<ReportEntity>> getAllReports(@AuthenticationPrincipal CustomUserDetails customUserDetails,
//			@RequestParam("sortKey") String sortKey, @RequestParam("sortValue") String sortValue) {
//		// 유저정보 시큐리티 확인
//		String email = customUserDetails.getUsername();
//		Long userId = userRepository.findUserIdByEmail(email);
//		
//		// 유저의 신고 목록 조회 서비스 호출
//		List<ReportEntity> report = reportService.findAll(sortKey, sortValue);
//		return ResponseEntity.ok(report);
//	}

	// 모든 신고대상자확인
	@GetMapping("/all")
	public ResponseEntity<Page<ReportDTO>> getReports(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sortKey", defaultValue = "reportDate") String sortKey,
			@RequestParam(value = "sortValue", defaultValue = "desc") String sortValue) {

		// 정렬 방향과 키 설정
		Sort.Direction direction = Sort.Direction.fromString(sortValue);
		Sort sort = Sort.by(direction, sortKey);

		// 페이지 요청 생성
		Pageable pageable = PageRequest.of(page - 1, size, sort);

		// 신고 리스트 조회
		Page<ReportDTO> reports = reportService.getReports(pageable, sortKey, sortValue);

		// 결과 반환
		return ResponseEntity.ok(reports);
	}

	// 특정 사용자의 신고자 목록 조회
	@GetMapping("/user")
	public ResponseEntity<List<ReportEntity>> blockForUser(
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		// 유저정보 시큐리티 확인
		String email = customUserDetails.getUsername();
		Long userId = userRepository.findUserIdByEmail(email);

		// 유저의 신고 목록 조회 서비스 호출
		List<ReportEntity> report = reportService.reportForUser(userId);
		return ResponseEntity.ok(report);
	}

	// 관리자용 신고 확인
	@PostMapping("/ok")
	public ResponseEntity<ReportEntity> updateReportFalse(@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@RequestBody ReportUpdateRequest request) {
		// 유저정보 시큐리티 확인
		String email = customUserDetails.getUsername();
		Long userId = userRepository.findUserIdByEmail(email);
		
		ReportEntity updatedReport = reportService.updateReportFalse(request.getReportId(),
				request.getReportFalseValue());
		return ResponseEntity.ok(updatedReport);
	}

}
