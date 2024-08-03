package com.example.tripit.mypage.controller;

import com.example.tripit.community.dto.CommunityDTO;
import com.example.tripit.error.CustomException;
import com.example.tripit.error.ErrorCode;
import com.example.tripit.mypage.dto.PasswordDTO;
import com.example.tripit.mypage.dto.ProfileDTO;
import com.example.tripit.mypage.dto.ProfileUpdateDTO;
import com.example.tripit.mypage.service.MyPageService;
import com.example.tripit.schedule.dto.DetailScheduleDto;
import com.example.tripit.schedule.dto.ScheduleDto;
import com.example.tripit.schedule.dto.ScheduleRequest;
import com.example.tripit.user.dto.CustomUserDetails;
import com.example.tripit.user.dto.UserDTO;
import com.example.tripit.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mypage")
public class MyPageController {


    private final UserRepository userRepository;

    private final MyPageService myPageService;

    @Autowired
    public MyPageController(UserRepository userRepository, MyPageService myPageService) {
        this.userRepository = userRepository;
        this.myPageService = myPageService;
    }

    @GetMapping("profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);
        ProfileDTO profileDTO = myPageService.getUserDTOById(userId);
            return ResponseEntity.ok(profileDTO);
    }

    @PatchMapping("profile/profileUpdate")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ProfileUpdateDTO profileUpdateDTO) {
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);
        myPageService.profileUpdate(profileUpdateDTO, userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("profile/passwordCheck")
    public ResponseEntity<Void> checkPassword(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody PasswordDTO dto) {
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);
        if (myPageService.passwordCheck(dto, userId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("profile/passwordUpdate")
    public ResponseEntity<String> passwordUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody PasswordDTO dto) {
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);
        return myPageService.passwordUpdate(dto, userId);
    }


    @GetMapping("schedules") //전체 일정 목록
    public ResponseEntity<?> schedulesList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);
        List<ScheduleDto> scheduleDtos = myPageService.findScheduleList(userId);
        return ResponseEntity.ok(scheduleDtos);
    }

    @PostMapping("schedules/delete-schedules") //일정 복수 삭제 -> 목록에서 삭제할 때임
    public ResponseEntity<?> schedulesDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody List<Long> scheduleIds){
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);
        //System.out.println(scheduleIds);
        //System.out.println("삭제요청");

        List<ScheduleDto> scheduleDtos = myPageService.schedulesDelete(scheduleIds, userId);
        return ResponseEntity.ok(scheduleDtos);
    }

    @GetMapping("schedules/{scheduleId}") //상세 일정
    public ResponseEntity<?> detailSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @PathVariable Long scheduleId) {
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);
        List<DetailScheduleDto> detailScheduleDtos = myPageService.detailSchedule(scheduleId);
        System.out.println(detailScheduleDtos);
        return ResponseEntity.ok(detailScheduleDtos);
    }

    @PatchMapping("update-schedules")
    public ResponseEntity<?> saveSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ScheduleRequest scheduleRequest) {
        //CustomUserDetails에서 userId 추출
        String email = customUserDetails.getUsername();
        long userId = userRepository.findUserIdByEmail(email);

        try {
            List<ScheduleDto> scheduleDtos = myPageService.updateSchedule(scheduleRequest, userId);

            return ResponseEntity.ok(scheduleDtos);
        } catch (Exception e) {
            // 클라이언트에게 전달할 에러 메시지
            String errorMessage = "일정 저장 실패: " + e.getMessage();
            //ErrorResponse result = new ErrorResponse(ErrorCode.SCHEDULE_FAIL);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    @DeleteMapping("schedules/{scheduleId}") //상세 페이지에서 삭제할 때
    public ResponseEntity<Void> scheduleDelete(@PathVariable Long scheduleId) {
            myPageService.scheduleDelete(scheduleId);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("postList")
    public ResponseEntity<?> postList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);
        List<CommunityDTO> postList = myPageService.postList(userId);

        return ResponseEntity.ok(postList);
    }

    @PostMapping("postList/delete-post")
    public ResponseEntity<?> postDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody List<Long> postIds) {

        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);

        List<CommunityDTO> postList = myPageService.postDelete(postIds, userId);
        return ResponseEntity.ok(postList);

    }

}
