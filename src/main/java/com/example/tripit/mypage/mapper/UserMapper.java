package com.example.tripit.mypage.mapper;

import com.example.tripit.mypage.dto.ProfileUpdateDTO;
import com.example.tripit.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "nickname", source = "dto.nickname", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "intro", source = "dto.intro", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userpic", source = "dto.userpic", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(ProfileUpdateDTO dto, @MappingTarget UserEntity entity);
}
