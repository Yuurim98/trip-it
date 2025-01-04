package com.example.tripit.schedule.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tripit.schedule.dto.ScheduleDto;
import com.example.tripit.schedule.dto.ScheduleRequest;
import com.example.tripit.schedule.service.ApiConnection;
import com.example.tripit.schedule.service.ScheduleService;
import com.example.tripit.user.dto.CustomUserDetails;
import com.example.tripit.user.repository.UserRepository;


@RestController
@RequestMapping("/home")
public class ScheduleController {

    private final ApiConnection apiConnection; //외부 Api 연결 담당 서비스 클래스
    private final ScheduleService scheduleService; //일정 관련 비즈니스 로직을 담당 서비스 클래스
    private final UserRepository userRepository;


    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    public ScheduleController(ApiConnection apiConnection, ScheduleService scheduleService, UserRepository userRepository) {
        this.apiConnection = apiConnection;
        this.scheduleService = scheduleService;
        this.userRepository = userRepository;
    }

    @GetMapping({"/apiList/{metroId}/{pageNo}", "/apiList/{metroId}/{pageNo}/{contentTypeId}"})
    public ResponseEntity<Object> travelApi(@PathVariable String metroId,
                                            @PathVariable String pageNo,
                                            @PathVariable(required = false) String contentTypeId) {
        //contentTypeId가 제공되지 않았을 때 기본값 설정
        if (contentTypeId == null) {
            contentTypeId = "12";
        }
        System.out.println("호출");
        //apiConnection 메서드 호출 및 반환
        return apiConnection.cultureFacilityApi(metroId, pageNo, contentTypeId);
    }

    @GetMapping("/apiDetail/{contentId}")
    public ResponseEntity<Object> apiTest2(@PathVariable String contentId) {
        System.out.println("장소 호출");
        return apiConnection.detailApi(contentId);
    }

    @GetMapping("/apiSearch/{metroId}/{pageNo}/{contentTypeId}/{keyword}")
    public ResponseEntity<Object> apiTest3(@PathVariable String metroId,
                                           @PathVariable String pageNo,
                                           @PathVariable String contentTypeId,
                                           @PathVariable String keyword) throws UnsupportedEncodingException {
        return apiConnection.searchApi(metroId, pageNo, contentTypeId, keyword);
    }

    @PostMapping("/saveSchedule")
    public ResponseEntity<?> saveSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ScheduleRequest scheduleRequest) {
        //CustomUserDetails에서 userId 추출
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findUserIdByEmail(email);

        //일정 저장 및 결과 반환
        List<ScheduleDto> scheduleDtoList = scheduleService.saveSchedule(scheduleRequest, userId);
        return ResponseEntity.ok(scheduleDtoList);
    }


//    @GetMapping("/{scheduleId}")
//    public ResponseEntity<?> getDetailSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long scheduleId) {
//        String email = customUserDetails.getUsername();
//        Integer userId = userRepository.findUserIdByEmail(email);
//        System.out.println(userId);
//        return scheduleService.detailSchedule(userId, scheduleId);
//    }



}
