package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.InterestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API endpoint for managing user interests.
 *
 * <p>This class provides an endpoint for adding interests to a user's profile.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/interests")
public class InterestEndpoint {

    private final InterestsService interestsService;

    /**
     * Adds an interest to the user's profile.
     *
     * @param interest    The interest to be added to the user's profile.
     * @param currentUser The currently authenticated user (obtained from the security context).
     * @return ResponseEntity with a success message if the interest is added successfully.
     */
    @PostMapping("/add")
    public ResponseEntity<String> interestsAdd(@RequestParam("interest") String interest,
                                               @AuthenticationPrincipal CurrentUser currentUser) {
        interestsService.interestSave(interest, currentUser);
        return new ResponseEntity<>("interest added to user", HttpStatus.OK);
    }
}
