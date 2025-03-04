package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.dto.userDto.UserUpdateRequestDto;
import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserImage;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TimelineService {
    List<Country> findAllCountries();

    User updateUser(UserUpdateRequestDto user, CurrentUser currentUser);

    User updateUserProfilePic(MultipartFile profilePic, CurrentUser currentUser, UserImage userImage);

    User updateUserProfileBackgroundPic(MultipartFile bgPic, CurrentUser currentUser);
}
