package com.example.tripit.schedule.service;

import com.example.tripit.error.CustomException;
import com.example.tripit.error.ErrorCode;
import com.example.tripit.schedule.dto.DetailScheduleDto;
import com.example.tripit.schedule.dto.ScheduleDto;
import com.example.tripit.schedule.dto.ScheduleRequest;
import com.example.tripit.schedule.entity.DetailScheduleEntity;
import com.example.tripit.schedule.entity.ScheduleEntity;
import com.example.tripit.schedule.repository.DetailScheduleRepository;
import com.example.tripit.schedule.repository.ScheduleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DetailScheduleRepository detailScheduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public List<ScheduleDto> saveSchedule(ScheduleRequest scheduleRequest, Long userId) {
        ScheduleDto scheduleDto = scheduleRequest.getScheduleDto();
        scheduleDto.setUserId(userId);

        try {
            //ModelMapper를 사용하여 DTO를 엔티티로 변환
            System.out.println("저장");
            //전체 일정 변환 및 저장
            ScheduleEntity saveScheduleEntity = modelMapper.map(scheduleDto, ScheduleEntity.class);
            saveScheduleEntity.setRegisterDate(LocalDate.now()); //현재시간으로 set
            //전체 일정 DB 저장
            scheduleRepository.save(saveScheduleEntity);

            //자동 생성된 PK값 변수 저장
            Long scheduleId = saveScheduleEntity.getScheduleId();

            //전체 일정 저장 성공 여부 확인
            if (scheduleId == null || scheduleId <= 0) {
                throw new RuntimeException("전체 일정 저장 실패. scheduleId 생성되지 않음");
            }

            //상세 일정 변환 및 저장
            List<DetailScheduleDto> detailScheduleDtoList = scheduleRequest.getDetailScheduleDto();

            //반복문 사용한 엔티티 매핑
            for (DetailScheduleDto detailScheduleDto : detailScheduleDtoList) {
                DetailScheduleEntity detailScheduleEntity = modelMapper.map(detailScheduleDto, DetailScheduleEntity.class);
                detailScheduleEntity.setRegisterTime(LocalDateTime.now());
                detailScheduleEntity.setScheduleId(scheduleId);
                detailScheduleRepository.save(detailScheduleEntity);
            }

            //저장된 일정을 조회 및 DTO로 반환
            List<ScheduleEntity> scheduleEntities = scheduleRepository.findByUser_UserIdOrderByScheduleIdDesc(userId);
            return scheduleEntities.stream()
                    .map(scheduleEntity -> modelMapper.map(scheduleEntity, ScheduleDto.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_SAVE_SCHEDULE);
        }
    }

//    public ResponseEntity<?> detailSchedule(long userId, Long scheduleId) {
//        // 1. ScheduleEntity 조회
//        Optional<ScheduleEntity> scheduleEntityOpt = scheduleRepository.findByUserIdAndScheduleId(userId, scheduleId);
//        if (scheduleEntityOpt.isEmpty()) {
//            // ScheduleEntity가 없을 경우 처리
//            return ResponseEntity.notFound().build();
//        }
//
//        // 2. DetailScheduleEntity 조회
//        List<DetailScheduleEntity> detailSchedules = detailScheduleRepository.findByScheduleId(scheduleId);
//
//        // 조회된 데이터 반환
//        return ResponseEntity.ok(detailSchedules);
//    }

}
