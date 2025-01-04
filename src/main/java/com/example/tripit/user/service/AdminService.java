package com.example.tripit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tripit.user.dto.UserDTO;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;

@Service
public class AdminService {

//	@Autowired
//	private UserRepository userRepository;
//
//	// 관리자용 전체 조회(페이징 및 검색)
//	public Page<UserDTO> getusers(String search, Pageable pageable, String sortKey, String sortValue) {
//	    Page<UserEntity> userPage;
//
//	    // 검색어에 따라 조회
//	    if (search != null && !search.isEmpty()) {
//	        userPage = userRepository.findBySearchTerm(search, pageable);
//	    } else {
//	        userPage = userRepository.findAll(pageable);
//	    }
//
//	 // DTO로 변환
//	    return userPage.map(user -> new UserDTO(
//	            user.getUserId(),
//	            user.getEmail(),
//	            user.getUsername(),
//	            user.getNickname(),
//	            user.getPassword(),
//	            user.getBirth(),
//	            user.getGender(),
//	            user.getIntro(),
//	            user.getRole(),
//	            user.getRegdate(),
//	            user.getUserpic(),
//	            user.getSocialType(),
//	            user.getReportCount()
//	    ));
//	}

	
	

}
