package com.example.tripit.block.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tripit.block.dto.BlockListDTO;
import com.example.tripit.block.entity.BlockListEntity;
import com.example.tripit.block.repository.BlockListRepository;
import com.example.tripit.error.CustomException;
import com.example.tripit.error.ErrorCode;
import com.example.tripit.user.dto.UserDTO;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BlockListService {
	@Autowired
	final BlockListRepository blockListRepository;

	@Autowired
	private UserRepository userRepository;

	public BlockListService(BlockListRepository blockListRepository) {
		this.blockListRepository = blockListRepository;
	}

	// 차단 추가 메서드
	public BlockListEntity addBlock(Long userId, String nickname) {
		// 유저 엔티티형식의 객체 생성
		UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// 이미 차단된 상대방의 닉네임인지 확인
		if (blockListRepository.existsByUserIdAndNickname(user, nickname)) {
			throw new CustomException(ErrorCode.BLOCK_EXISTS2);
		} // if() end

		// 블록리스트 객체 생성
		BlockListEntity blockList = new BlockListEntity();
		blockList.setUserId(user);
		blockList.setNickname(nickname);

		// 블록리스트 저장
		return blockListRepository.save(blockList);
	}

	// mypage 조회
	public List<BlockListEntity> mypageblockForUser(Long userId) {
		UserEntity user = userRepository.findById(userId).orElse(null);
		return blockListRepository.findByUserId(user); // 빈 리스트 반환
	}

	// sort 특정 사용자의 차단 목록 조회 메서드
	public List<BlockListEntity> getblockForUser(String sortKey, String sortValue, Long userId) {
		Sort sort = Sort.by(Sort.Direction.fromString(sortValue), sortKey);
		UserEntity user = userRepository.findById(userId).orElse(null);

		if (user != null) {
			return blockListRepository.findByUserId(user, sort);
		}
		return List.of(); // 빈 리스트 반환
	}

	// sort 모든 사용자의 차단 목록 조회 메서드
	public List<BlockListEntity> getBlockForAdmin(String sortKey, String sortValue) {

		// Sort 객체 생성
		Sort sort = Sort.by(Sort.Direction.fromString(sortValue), sortKey);
		// 모든 차단 목록 항목을 정렬하여 반환
		return blockListRepository.findAll(sort);
	}

	// UserDTO로 변환하는 메소드
	private UserDTO convertToUserDTO(UserEntity userEntity) {
		UserDTO userDTO = new UserDTO();
		// UserEntity의 속성들을 UserDTO에 매핑
		userDTO.setUserId(userEntity.getUserId());
		userDTO.setNickname(userEntity.getNickname());
		return userDTO;
	}

	// DTO로 변환하는 메소드
	public BlockListDTO convertToBlockListDTO(BlockListEntity BlockListEntity) {
		BlockListDTO dto = new BlockListDTO();
		dto.setBlockId(BlockListEntity.getBlockId());
		dto.setUserId(convertToUserDTO(BlockListEntity.getUserId()));
		dto.setNickname(BlockListEntity.getNickname());
		dto.setBlockDate(BlockListEntity.getBlockDate());
		// createdDate는 실제로 필요하지 않으면 설정하지 않을 수 있습니다.
		return dto;
	}

	// DTO방식으로 조회,정렬,페이징
	public Page<BlockListDTO> blockListPage(String search, String sortKey, String sortValue, int page, int size) {
//		 // 기본 정렬 필드 및 방향 설정
		String defaultSortKey = "blockDate";
		String defaultSortValue = "desc";

//		// 검색 키워드가 없는 경우 빈 페이지 반환
//		if (search == null || search.trim().isEmpty()) {
//			Pageable pageable = PageRequest.of(page - 1, size);
//			return new PageImpl<>(Collections.emptyList(), pageable, 0);
//		}

		// Sort 객체 설정
		Sort.Direction direction = Sort.Direction
				.fromString(sortValue != null && !sortValue.isEmpty() ? sortValue : defaultSortValue);

		// 정렬 필드 설정
		String sortingField = switch (sortKey != null ? sortKey : "") {
		case "userId" -> "userId.nickname"; // 연관된 UserEntity의 nickname
		case "nickname" -> "nickname";
		case "blockDate" -> "blockDate";
		default -> defaultSortKey; // 기본값
		};

		// 적절한 정렬 키 설정
		if (sortKey == null || sortKey.isEmpty()) {
			sortingField = defaultSortKey;
		} else if (sortKey.equals("userId")) {
			sortingField = "userId.nickname"; // 연관된 UserEntity의 nickname
		} else if (sortKey.equals("nickname")) {
			sortingField = "nickname"; // BlockListEntity의 blockDate
		} else if (sortKey.equals("blockDate")) {
			sortingField = "blockDate";
		} else {
			sortingField = sortKey; // 기본적으로 sortKey로 설정된 필드를 사용
		}

		// 복합 정렬 필드 설정
		Sort sort;
		if ("userId".equals(sortKey) || "blockDate".equals(sortKey)) {
			// 복합 정렬 필드 설정 예시
			// userId.nickname, blockDate 필드를 기준으로 정렬
			sort = Sort.by(Sort.Order.by("userId.nickname").with(direction))
					.and(Sort.by(Sort.Order.by("blockDate").with(direction)));
		} else {
			// 단일 정렬 필드 설정
			sort = Sort.by(Sort.Order.by(sortingField).with(direction));
		}

		// 페이징 설정
		Pageable pageable = PageRequest.of(page - 1, size, sort);

		// 검색 수행
		Page<BlockListEntity> entityPage;
		if (search != null && !search.trim().isEmpty()) {
			// 날짜 형식으로 검색 시 필터링 적용
			if (isDate(search)) {
				LocalDateTime searchDate = parseDate(search);
				entityPage = blockListRepository.findByBlockDate(searchDate, pageable);
			} else {
				entityPage = blockListRepository.findByNicknameContainingIgnoreCase(search, pageable);
			}
		} else {
			entityPage = blockListRepository.findAll(pageable);
		}
		return entityPage.map(this::convertToBlockListDTO);
	}

	// 날짜 형식 확인
	private boolean isDate(String value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // 예시 형식, 필요에 따라 조정
		try {
			LocalDateTime.parse(value, formatter);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	// 문자열을 Date로 변환
	private LocalDateTime parseDate(String value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // 필요한 포맷으로 설정
		try {
			return LocalDateTime.parse(value, formatter);
		} catch (DateTimeParseException e) {
			return null; // 파싱 오류 시 null 반환
		}
	}

	// 차단 목록 전체조회
	public List<BlockListEntity> findByUserId(UserEntity userId) {
		return blockListRepository.findByUserId(userId);
	}

	// 차단 삭제(해제)
	@Transactional
	public void deleteForUser(Long userId, Long blockId) {
		// 유저 엔티티 확인
		UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		// 이미 차단된 상대방의 닉네임인지 확인
		if (!blockListRepository.existsByUserIdAndBlockId(user, blockId)) {
			throw new CustomException(ErrorCode.BLOCK_EXISTS3);
		} // if() end

		// 블록시스트 객체 생성
		BlockListEntity blockList = new BlockListEntity();
		blockList.setUserId(user);
		blockList.setBlockId(blockId);
		// 차단 리스트에서 항목 삭제
		blockListRepository.deleteByUserIdAndBlockId(user, blockId);

	}
}
