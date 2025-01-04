package com.example.tripit.mypage.service;

import com.example.tripit.community.dto.CommunityDTO;
import com.example.tripit.community.dto.MetroENUM;
import com.example.tripit.community.entity.PostEntity;
import com.example.tripit.community.repository.PostRepository;
import com.example.tripit.error.CustomException;
import com.example.tripit.error.ErrorCode;
import com.example.tripit.mypage.dto.PasswordDTO;
import com.example.tripit.mypage.dto.ProfileDTO;
import com.example.tripit.mypage.dto.ProfileUpdateDTO;
import com.example.tripit.mypage.mapper.DetailScheduleMapper;
import com.example.tripit.mypage.mapper.ScheduleMapper;
import com.example.tripit.mypage.mapper.UserMapper;
import com.example.tripit.schedule.dto.DetailScheduleDto;
import com.example.tripit.schedule.dto.ScheduleDto;
import com.example.tripit.schedule.dto.ScheduleRequest;
import com.example.tripit.schedule.entity.DetailScheduleEntity;
import com.example.tripit.schedule.entity.ScheduleEntity;
import com.example.tripit.schedule.repository.DetailScheduleRepository;
import com.example.tripit.schedule.repository.ScheduleRepository;
import com.example.tripit.user.dto.UserDTO;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DetailScheduleRepository detailScheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private DetailScheduleMapper detailScheduleMapper;

    public MyPageService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //userId 엔티티 찾기
    public ProfileDTO getUserDTOById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ProfileDTO profileDTO = modelMapper.map(userEntity, ProfileDTO.class);

        if (profileDTO.getIntro() == null) {
            profileDTO.setIntro("");
        }

        if (profileDTO.getUserpic() == null) {
            profileDTO.setUserpic("");
        }

        return profileDTO;
    }

    //user엔티티를 DTO로 변환하기
    private ProfileDTO mapToUserDTO(UserEntity userEntity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setUserId(userEntity.getUserId()); //id
        dto.setNickname(userEntity.getNickname()); //닉네임
        dto.setUsername(userEntity.getUsername()); //이름
        dto.setIntro(Optional.ofNullable(userEntity.getIntro()).orElse("")); //자기소개
        dto.setUserpic(Optional.ofNullable(userEntity.getUserpic()).orElse("")); //사진
        dto.setGender(userEntity.getGender()); //성별
        dto.setEmail(userEntity.getEmail()); //이메일
        dto.setBirth(userEntity.getBirth()); //생년월일
        dto.setRole(userEntity.getRole()); //권한
        //dto.setRegdate(userEntity.getRegdate()); //가입일자
        dto.setSocial_type(userEntity.getSocialType()); //소셜타입
        //dto.setReportCount(userEntity.getReportCount()); //신고누적

        return dto;
    }

    //프로필 수정
    @Transactional
    public void profileUpdate(ProfileUpdateDTO profileUpdateDTO, Long userId) {

        //닉네임 중복 검증
        if (isNicknameExists(profileUpdateDTO.getNickname(), userId)) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAMES); //존재하는 닉네임 있음
        }

        //글자수 검증
        if (profileUpdateDTO.getIntro() != null) {
            String intro = profileUpdateDTO.getIntro();
            if (intro.length() > 100) {
                throw new CustomException(ErrorCode.INTRO_TOO_LONG); //100자 이상
            }
        }

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 엔티티가 존재하지 않음

        userMapper.updateUserFromDto(profileUpdateDTO, userEntity);
        userRepository.save(userEntity);

    }

    public boolean isNicknameExists(String nickname, Long userId) {
        return userRepository.existsByNicknameAndUserIdNot(nickname, userId);
    }

    //비밀번호 확인
    public boolean passwordCheck(PasswordDTO dto, Long userId) {
        return userRepository.findById(userId)
                .map(userEntity -> bCryptPasswordEncoder.matches(dto.getPassword(), userEntity.getPassword()))
                .orElse(false);
    }

    //비밀번호 수정
    @Transactional
    public ResponseEntity<String> passwordUpdate(PasswordDTO dto, Long userId) {
        return userRepository.findById(userId).map(userEntity -> {
            userEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
            return ResponseEntity.ok("수정완료");
        }).orElseThrow(() -> new EntityNotFoundException("user가 존재하지 않음 : " + userId));
    }

    //전체 일정 목록
    public List<ScheduleDto> findScheduleList(Long userId) {
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByUser_UserIdOrderByScheduleIdDesc(userId);

        return scheduleEntities.stream()
                .map(scheduleEntity -> modelMapper.map(scheduleEntity, ScheduleDto.class))
                .collect(Collectors.toList());
    }

    //상세 일정
    public List<DetailScheduleDto> detailSchedule(Long scheduleId) {
        // DetailScheduleEntity 조회
        List<DetailScheduleEntity> detailSchedulesEntities = detailScheduleRepository.findByScheduleId(scheduleId);
        if (detailSchedulesEntities.isEmpty()) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);

        }
        // 조회된 데이터 반환
        return detailSchedulesEntities.stream()
                .map(detailScheduleEntity -> modelMapper.map(detailScheduleEntity, DetailScheduleDto.class))
                .collect(Collectors.toList());
    }

    //목록에서 일정 삭제
    @Transactional
    public List<ScheduleDto> schedulesDelete(List<Long> scheduleIds, Long userId) {
        List<ScheduleEntity> schedules = scheduleRepository.findAllById(scheduleIds);

        if (scheduleIds.size() != schedules.size() || schedules.isEmpty()) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        scheduleRepository.deleteAllById(scheduleIds);

        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByUser_UserIdOrderByScheduleIdDesc(userId);

        return scheduleEntities.stream()
                .map(scheduleEntity -> modelMapper.map(scheduleEntity, ScheduleDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ScheduleDto> updateSchedule(ScheduleRequest scheduleRequest, Long userId) {
        ScheduleDto scheduleDto = scheduleRequest.getScheduleDto();
        scheduleDto.setUserId(userId);
        try {
            System.out.println("시도");
            System.out.println(userId);
            ScheduleEntity updateScheduleEntity = scheduleRepository.findById(scheduleDto.getScheduleId())
                    .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

            System.out.println("전체일정");
            scheduleMapper.updateScheduleFromDto(scheduleDto, updateScheduleEntity);

            updateScheduleEntity.setRegisterDate(LocalDate.now());
            Long scheduleId = updateScheduleEntity.getScheduleId();
            //엔티티 DB 저장
            scheduleRepository.save(updateScheduleEntity);

            //기존 상세일정 가져오기
            List<DetailScheduleEntity> existingDetailSchedules = detailScheduleRepository.findByScheduleId(scheduleId);

            //scheduleRequest에서 상세일정id 목록 추출
            Set<Long> requestedDetailIds = scheduleRequest.getDetailScheduleDto().stream()
                    .map(DetailScheduleDto::getScheduleDetailId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            //scheduleRequest에 포함되지 않는 상세일정id 목록 추출
            List<Long> idsToDelete = existingDetailSchedules.stream()
                    .map(DetailScheduleEntity::getScheduleDetailId)
                    .filter(id -> !requestedDetailIds.contains(id))
                    .collect(Collectors.toList());

            //삭제
            if (!idsToDelete.isEmpty()) {
                detailScheduleRepository.deleteAllById(idsToDelete);
            }

            List<DetailScheduleDto> detailScheduleDtoList = scheduleRequest.getDetailScheduleDto();

            for (DetailScheduleDto detailScheduleDto : detailScheduleDtoList) {
                if (detailScheduleDto.getScheduleDetailId() != null) {
                    System.out.println("상세일정");

                    DetailScheduleEntity detailScheduleEntity = detailScheduleRepository.findById(detailScheduleDto.getScheduleDetailId())
                            .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

                    detailScheduleMapper.updateDetailScheduleFromDto(detailScheduleDto, detailScheduleEntity);
                    detailScheduleEntity.setRegisterTime(LocalDateTime.now());
                    detailScheduleEntity.setScheduleId(scheduleId);
                    detailScheduleRepository.save(detailScheduleEntity);
                }else {
                    DetailScheduleEntity newDetailScheduleEntity = new DetailScheduleEntity();
                    detailScheduleMapper.updateDetailScheduleFromDto(detailScheduleDto, newDetailScheduleEntity);
                    newDetailScheduleEntity.setRegisterTime(LocalDateTime.now());
                    newDetailScheduleEntity.setScheduleId(scheduleId);
                    detailScheduleRepository.save(newDetailScheduleEntity);
                }
            }

            List<ScheduleEntity> scheduleEntities = scheduleRepository.findByUser_UserIdOrderByScheduleIdDesc(userId);
            return scheduleMapper.toDtoList(scheduleEntities);
            //            return scheduleEntities.stream()
//                    .map(scheduleEntity -> modelMapper.map(scheduleEntity, ScheduleDto.class))
//                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            System.out.println("실패");
            throw new RuntimeException("일정 저장 실패", e);

        }
    }

    //상세 페이지에서 일정 삭제
    @Transactional
    public void scheduleDelete(Long scheduleId) {
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(scheduleId);
        if (scheduleEntity.isEmpty()) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        scheduleRepository.deleteById(scheduleId);
    }

    //글 목록
    public List<CommunityDTO> postList(Long userId) {
        UserEntity user = new UserEntity();
        user.setUserId(userId);

        List<PostEntity> posts = postRepository.findByUserId(user);
        return posts.stream()
                .map(post -> {
                    UserEntity userEntity = post.getUserId();
                    ScheduleEntity schedule = post.getScheduleId();

                    return new CommunityDTO(
                            post.getPostId(),
                            post.getPostTitle(),
                            post.getPostContent(),
                            post.getPersonnel(),
                            post.getViewCount(),
                            post.getExposureStatus(),
                            post.getPostPic(),
                            post.getPostDate(),
                            userEntity.getUserId(),
                            userEntity.getNickname(),
                            userEntity.getGender(),
                            userEntity.getBirth(),
                            userEntity.getUserpic(),
                            schedule.getScheduleId(),
                            schedule.getMetroId(),
                            MetroENUM.getNameById(schedule.getMetroId()),
                            schedule.getStartDate(),
                            schedule.getEndDate()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CommunityDTO> postDelete(List<Long> postIds, Long userId) {

        List<PostEntity> postList = postRepository.findAllById(postIds);

        if (postList.size() != postIds.size() || postList.isEmpty()) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        postRepository.deleteAllById(postIds);

        return postList(userId);

    }

    public void userWithdrawal() {

    }
}
