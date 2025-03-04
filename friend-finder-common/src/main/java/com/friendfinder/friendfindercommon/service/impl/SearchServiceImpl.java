package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * SearchServiceImpl is a service class that provides methods for searching users in the application.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>userRepository: The UserRepository interface used to access and retrieve user-related data from the database.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>searchByKeyword(keyword, currentUser, currentPage): Searches users by a keyword and returns a Page of User objects.
 *     The method fetches users whose names or surnames contain the given keyword, and the results are paginated based on the currentPage number.
 *     It returns a Page object containing the search results, and currentUser is used to exclude the current user from the search results.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * SearchServiceImpl provides functionality for searching users in the application based on a keyword. The search can be performed
 * by the user to find other users whose names or surnames match the provided keyword. The search results are paginated to improve
 * the user experience and display a limited number of users per page. The service ensures that the current user is excluded from
 * the search results to avoid duplicate entries. The implementation is designed to handle user searches efficiently and provide
 * relevant user suggestions to users in the social media application.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final UserRepository userRepository;

    @Override
    public Page<User> searchByKeyword(String keyword, CurrentUser currentUser, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, 2);
        Page<User> byNameContainsOrSurnameContains = userRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(keyword, keyword, pageable);
        return byNameContainsOrSurnameContains;
    }

}
