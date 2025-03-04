package com.friendfinder.friendfindercommon.mapper;

import com.friendfinder.friendfindercommon.dto.userDto.UserDto;
import com.friendfinder.friendfindercommon.dto.userDto.UserRegisterRequestDto;
import com.friendfinder.friendfindercommon.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRegisterMapper {
    User map(UserRegisterRequestDto requestDto);

    UserDto mapToUserDto(User user);

}
