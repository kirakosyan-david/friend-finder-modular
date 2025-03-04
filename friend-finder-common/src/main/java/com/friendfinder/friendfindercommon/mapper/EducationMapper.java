package com.friendfinder.friendfindercommon.mapper;

import com.friendfinder.friendfindercommon.dto.workEduDto.EducationCreateRequestDto;
import com.friendfinder.friendfindercommon.dto.workEduDto.EducationCreateResponseDto;
import com.friendfinder.friendfindercommon.entity.Education;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EducationMapper {
    Education map(EducationCreateRequestDto educationDto);

    @Mapping(target = "userId", source = "user.id")
    EducationCreateResponseDto mapToResponseDto(Education education);
}
