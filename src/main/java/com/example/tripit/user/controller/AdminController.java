package com.example.tripit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.tripit.error.CustomException;
import com.example.tripit.error.ErrorCode;
import com.example.tripit.user.dto.UserDTO;
import com.example.tripit.user.service.AdminService;



@Controller
@ResponseBody
public class AdminController {

	@Autowired
	private AdminService adminService;

    @GetMapping("/admin")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminP() {
        return "admin Controller";
    }
    
//    @GetMapping("/users")
//	public ResponseEntity<Page<UserDTO>> allUsers(
//	    @RequestParam(value = "search", required = false) String search,
//	    @RequestParam(value = "page", defaultValue = "1") int page,
//	    @RequestParam(value = "size", defaultValue = "10") int size,
//	    @RequestParam(value = "sortKey", defaultValue = "reportDate") String sortKey,
//	    @RequestParam(value = "sortValue", defaultValue = "desc") String sortValue
//	) {
//	    // 정렬 방향과 키 설정
//	    Sort.Direction direction = Sort.Direction.fromString(sortValue);
//	    Sort sort = Sort.by(direction, sortKey);
//
//	    // 페이지 요청 생성
//	    Pageable pageable = PageRequest.of(page - 1, size, sort);
//
//	    // 신고 리스트 조회
//	    Page<UserDTO> users = adminService.getusers(search, pageable, sortKey, sortValue);
//
//	    if (users.isEmpty()) {
//	        // 빈 결과에 대한 커스텀 예외를 던짐
//	        throw new CustomException(ErrorCode.SEARCH_EXISTS);
//	    }
//
//	    // 결과 반환
//	    return ResponseEntity.ok(users);
//	}
//    
}
