package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.entity.Education;
import com.friendfinder.friendfindercommon.security.CurrentUser;

public interface EducationService {

    Education saveEducation(Education education, CurrentUser currentUser);

}
