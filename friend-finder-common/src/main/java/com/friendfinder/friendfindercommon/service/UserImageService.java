package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.entity.UserImage;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserImageService {

    Page<UserImage> userImagePageByUserId(int userId, int pageNumber);

    List<UserImage> getUserImageById(int userId);

    void userImageSave(UserImage userImage, CurrentUser currentUser);

    UserImage deleteUserImageById(int id);
}
