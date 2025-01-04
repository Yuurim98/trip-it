package com.example.tripit.mypage.mapper;

import com.example.tripit.schedule.dto.DetailScheduleDto;
import com.example.tripit.schedule.dto.ScheduleDto;
import com.example.tripit.schedule.entity.DetailScheduleEntity;
import com.example.tripit.schedule.entity.ScheduleEntity;
import com.example.tripit.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "user.userId", source = "userId")
    void updateScheduleFromDto(ScheduleDto dto, @MappingTarget ScheduleEntity entity);

    @Mapping(source = "user.userId", target = "userId")
    ScheduleDto toDto(ScheduleEntity entity);

    List<ScheduleDto> toDtoList(List<ScheduleEntity> entities);

}
