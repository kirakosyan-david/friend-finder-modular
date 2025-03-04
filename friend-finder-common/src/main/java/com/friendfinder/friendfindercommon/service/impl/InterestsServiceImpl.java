package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.Interest;
import com.friendfinder.friendfindercommon.repository.InterestsRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.InterestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * InterestsServiceImpl is the implementation of the InterestsService interface, providing methods to interact with the
 * InterestsRepository and perform operations related to user interests.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>interestsRepository: The InterestsRepository interface, allowing this service to interact with the
 *     database to perform CRUD operations on Interest entities.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>findAllByUserId(userId): Retrieves a list of Interest objects associated with the specified userId. The method
 *     fetches interests from the database based on the given userId.</li>
 *     <li>interestSave(interest, currentUser): Saves a new Interest for the currentUser in the database. The method
 *     creates a new Interest object with the provided interest and the currentUser, then saves it to the database.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * InterestsServiceImpl is used to manage user interests in the application. It is typically utilized by the
 * application's endpoints or controllers that handle user interactions related to interests. For example, when a user
 * adds a new interest to their profile, this service is responsible for saving the interest to the database. Similarly,
 * when the application needs to retrieve a user's interests, this service is used to fetch the relevant data from the
 * database and return it to the calling component.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class InterestsServiceImpl implements InterestsService {

    private final InterestsRepository interestsRepository;

    @Override
    public List<Interest> findAllByUserId(int userId) {
        return interestsRepository.findAllByUserId(userId);
    }

    @Override
    public void interestSave(String interest, CurrentUser currentUser) {
        interestsRepository.save(Interest.builder()
                .interest(interest)
                .user(currentUser.getUser())
                .build());
    }
}
