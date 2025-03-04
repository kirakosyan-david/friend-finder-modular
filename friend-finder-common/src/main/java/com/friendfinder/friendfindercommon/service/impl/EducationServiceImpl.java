package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.Education;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.repository.EducationRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * EducationServiceImpl is the implementation of the EducationService interface, which provides methods to interact with
 * the EducationRepository and perform operations related to a user's education details.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>educationRepository: The EducationRepository interface, which allows this service to interact with the
 *     database to perform CRUD operations on the Education entity.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>saveEducation(education, currentUser): Saves the user's education details to the database. It takes an
 *     Education object containing the education details and the current authenticated user (CurrentUser). The method
 *     associates the education with the user, sets the appropriate fields (such as degree, institution, and date of
 *     completion), and saves the education to the database using the educationRepository.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * EducationServiceImpl is used to manage operations related to a user's education information. It is typically used by
 * the application's endpoints or controllers that handle user profile settings and updates related to education
 * details. When a user updates or adds new education information, this service is responsible for saving the details
 * to the database.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;

    @Override
    public Education saveEducation(Education education, CurrentUser currentUser) {
        User user = currentUser.getUser();
        education.setUser(user);
        return educationRepository.save(education);
    }
}
