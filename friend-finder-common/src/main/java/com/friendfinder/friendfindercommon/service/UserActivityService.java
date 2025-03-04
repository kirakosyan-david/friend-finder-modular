package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserActivity;

import java.util.List;

public interface UserActivityService {
    List<UserActivity> getAllByUserId(int userId);

    void save(User user, String type);
}
