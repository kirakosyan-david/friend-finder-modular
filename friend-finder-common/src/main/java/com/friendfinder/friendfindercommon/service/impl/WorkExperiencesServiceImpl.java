package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.WorkExperiences;
import com.friendfinder.friendfindercommon.repository.WorkExperiencesRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.WorkExperiencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * WorkExperiencesServiceImpl is a service class that provides methods for managing work experiences-related functionalities in the application.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>workExperiencesRepository: The WorkExperiencesRepository interface used to access and retrieve work experiences data from the database.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>findAllByUserId(userId): Retrieves a list of work experiences for a specific user from the database based on the provided user ID.</li>
 *     <li>saveWorkExperiences(workExperiences, currentUser): Saves a new work experience entry for the current user in the application.
 *     The method associates the work experience with the current user and saves it to the database using the WorkExperiencesRepository.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * WorkExperiencesServiceImpl provides functionalities for managing work experiences of users in the application.
 * It allows users to retrieve their work experiences, as well as add new work experience entries to their profiles.
 * The service communicates with the WorkExperiencesRepository to access and modify work experiences data in the database.
 * By utilizing these functionalities, users can maintain and showcase their professional work experiences within the application,
 * contributing to the completeness and credibility of their profiles.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class WorkExperiencesServiceImpl implements WorkExperiencesService {

    private final WorkExperiencesRepository workExperiencesRepository;

    @Override
    public List<WorkExperiences> findAllByUserId(int userId) {
        return workExperiencesRepository.findAllByUserId(userId);
    }

    @Override
    public WorkExperiences saveWorkExperiences(WorkExperiences workExperiences, CurrentUser currentUser) {
        User user = currentUser.getUser();
        workExperiences.setUser(user);
        return workExperiencesRepository.save(workExperiences);
    }
}
