package com.friendfinder.friendfindercommon.mapper;

import com.friendfinder.friendfindercommon.dto.workEduDto.WorkExperiencesCreateRequestDto;
import com.friendfinder.friendfindercommon.dto.workEduDto.WorkExperiencesCreateResponseDto;
import com.friendfinder.friendfindercommon.entity.WorkExperiences;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkExperiencesMapper {
    WorkExperiences map(WorkExperiencesCreateRequestDto educationDto);

    @Mapping(target = "userId", source = "user.id")
    WorkExperiencesCreateResponseDto mapToResponseDto(WorkExperiences education);
}
