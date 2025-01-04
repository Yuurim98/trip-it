package com.example.tripit.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
    private ScheduleDto scheduleDto;
    private List<DetailScheduleDto> detailScheduleDto;
}
