package com.example.tripit.mypage.mapper;

import com.example.tripit.schedule.dto.DetailScheduleDto;
import com.example.tripit.schedule.dto.ScheduleDto;
import com.example.tripit.schedule.entity.DetailScheduleEntity;
import com.example.tripit.schedule.entity.ScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetailScheduleMapper {

    void updateDetailScheduleFromDto(DetailScheduleDto dto, @MappingTarget DetailScheduleEntity entity);

}
