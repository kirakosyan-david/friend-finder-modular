package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.entity.Interest;
import com.friendfinder.friendfindercommon.security.CurrentUser;

import java.util.List;

public interface InterestsService {
    List<Interest> findAllByUserId(int userId);

    void interestSave(String interest, CurrentUser currentUser);
}
