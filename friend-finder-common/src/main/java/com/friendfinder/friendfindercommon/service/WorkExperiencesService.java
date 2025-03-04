package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.entity.WorkExperiences;
import com.friendfinder.friendfindercommon.security.CurrentUser;

import java.util.List;

public interface WorkExperiencesService {
    List<WorkExperiences> findAllByUserId(int userId);

    WorkExperiences saveWorkExperiences(WorkExperiences workExperiences, CurrentUser currentUser);
}
