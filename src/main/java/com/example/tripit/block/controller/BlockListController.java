package com.example.tripit.block.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripit.block.dto.BlockListDTO;
import com.example.tripit.block.entity.BlockListEntity;
import com.example.tripit.block.repository.BlockListRepository;
import com.example.tripit.block.service.BlockListService;
import com.example.tripit.error.CustomException;
import com.example.tripit.error.ErrorCode;
import com.example.tripit.user.dto.CustomUserDetails;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;

@RestController
@RequestMapping("/block")
public class BlockListController {

	private final BlockListService blockListService;
	@Autowired
	UserRepository userRepository;

	public BlockListController(BlockListService blockListService, BlockListRepository blockListRepository) {
		this.blockListService = blockListService;
	}

	// 차단 추가
	@PostMapping("/add")
	public ResponseEntity<List<BlockListEntity>> addBlock(@RequestBody BlockListEntity blockList,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		// 유저정보 시큐리티 확인
		String email = customUserDetails.getUsername();
		long userId = userRepository.findUserIdByEmail(email);
		Optional<UserEntity> entity = userRepository.findById(userId);
		String nickname = entity.get().getNickname();

		// 자기 자신인지 확인
		if (nickname.equals(blockList.getNickname())) {
			throw new CustomException(ErrorCode.BLOCK_EXISTS);
		} // if end

		// 차단 추가 서비스 호출
		blockListService.addBlock(userId, blockList.getNickname());

		UserEntity user = new UserEntity();
		user.setUserId(userId);

		// 업데이트된 차단 리스트 가져오기
		List<BlockListEntity> updatedblockList = blockListService.findByUserId(user);

		// 업데이트된 차단 리스트 리턴
		return ResponseEntity.ok(updatedblockList);
	}

	@GetMapping("/all")
	public ResponseEntity<Page<BlockListDTO>> blockForAdmin(
			@RequestParam(value = "search", required = false) String search,
			@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("sortKey") String sortKey,
			@RequestParam("sortValue") String sortValue, @RequestParam("page") int page, @RequestParam("size") int size
	) {

		// 유저 정보 시큐리티 확인
		String email = customUserDetails.getUsername(); // email
		Long userId = userRepository.findUserIdByEmail(email);

		// 정렬된 차단 리스트 조회
		List<BlockListEntity> blockList = blockListService.getBlockForAdmin(sortKey, sortValue);
		// 페이징 및
		Page<BlockListDTO> blocksPage = blockListService.blockListPage(search, sortKey, sortValue, page, size);

		// 응답 객체 구성
		Map<String, Object> response = new HashMap<>();
		response.put("blocks", blockList);
		response.put("totalElements", blocksPage.getTotalElements());

		return ResponseEntity.ok(blocksPage);
	}

//    // 특정 사용자의 차단자 목록 조회
	@GetMapping("/user")
	public ResponseEntity<List<BlockListEntity>> blockForUser(
			@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("sortKey") String sortKey,
			@RequestParam("sortValue") String sortValue) {

		// 유저정보 시큐리티 확인
		String email = customUserDetails.getUsername();// email
		Long userId = userRepository.findUserIdByEmail(email);

		// 유저의 차단 목록 조회 서비스 호출
		// ProntEnd 에서 전달받은 userId ,sortKey, sortValue 값의 결과를 반환
		List<BlockListEntity> blockList = blockListService.getblockForUser(sortKey, sortValue, userId);
		return ResponseEntity.ok(blockList);
	}


	// 차단 삭제
	@PostMapping("/delete")
	public ResponseEntity<String> deleteBlock(@RequestBody BlockListEntity blockList,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		// 유저정보 시큐리티 확인
		String email = customUserDetails.getUsername();
		long userId = userRepository.findUserIdByEmail(email);

//        // 요청된 userId와 로그인된 userId가 일치하는지 확인
//        UserEntity user =  blockList.getUserId();
//        user.setUserId(userId);

		// 차단 삭제 서비스 호출
		blockListService.deleteForUser(userId, blockList.getBlockId());

		// 유저 객체생성후 user에 userId 담기
		UserEntity user = new UserEntity();
		user.setUserId(userId);

		// 업데이트된 차단 리스트 가져오기
		List<BlockListEntity> updatedblockList = blockListService.findByUserId(user);

		// 업데이트된 차단 리스트 리턴
		return ResponseEntity.ok("차단 해제 완료");
	}

//    // 특정 사용자의 차단자 목록 조회DTO
//    @GetMapping("/user")
//    public ResponseEntity<List<blockListDTO>> getblockForUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
//                                                                  @RequestParam("sortKey") String sortKey, 
//                                                                  @RequestParam("sortValue") String sortValue) {
//        // 유저정보 시큐리티 확인
//        String email = customUserDetails.getUsername();
//        Long userId = userRepository.findUserIdByEmail(email);
//z
//        // 유저의 차단 목록 조회 서비스 호출
//        List<blockListDTO> blockList = blockListService.getblockForUser(sortKey, sortValue, userId);
//        return ResponseEntity.ok(blockList);
//    }

}