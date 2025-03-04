package com.friendfinder.friendfindercommon.mapper;

import com.friendfinder.friendfindercommon.dto.userDto.UserDto;
import com.friendfinder.friendfindercommon.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    List<UserDto> mapToDtos(List<User> users);
}
