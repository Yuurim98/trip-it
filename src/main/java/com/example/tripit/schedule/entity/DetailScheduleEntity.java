package com.example.tripit.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SCHEDULE_DETAIL")
public class DetailScheduleEntity {

    @Id //PK 값 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 값 DB에서 자동 생성
    @Column(name = "schedule_detail_id")
    private Long scheduleDetailId;

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(nullable = false, name = "schedule_order")
    private int scheduleOrder;

    @Column(nullable = false, name = "start_time")
    private LocalTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalTime endTime;

    @Column(nullable = false, name = "content_id")
    private String contentId;

    @Column(nullable = false, name = "register_time")
    private LocalDateTime registerTime;
}