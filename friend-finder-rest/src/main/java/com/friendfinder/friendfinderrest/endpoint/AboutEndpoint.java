package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.userDto.AboutUserDto;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.exception.custom.ChangePasswordException;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.InterestsService;
import com.friendfinder.friendfindercommon.service.LanguageService;
import com.friendfinder.friendfindercommon.service.UserService;
import com.friendfinder.friendfindercommon.service.WorkExperiencesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * AboutEndpoint is a RESTful API controller class that handles HTTP requests related to user profile information and password change functionality.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>workExperiencesService: The WorkExperiencesService interface used to access and manage user work experiences.</li>
 *     <li>interestsService: The InterestsService interface used to access and manage user interests.</li>
 *     <li>languageService: The LanguageService interface used to access and manage user language information.</li>
 *     <li>userService: The UserService interface used to access and manage user-related information and actions.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>userInfo(user): Handles a GET request to retrieve and return information about a specific user's profile.
 *     The method calls the services for retrieving work experiences, interests, and language information for the user with the provided ID.</li>
 *     <li>changePassword(oldPass, newPass, confPass, currentUser): Handles a POST request to change the password for the current user.
 *     The method validates the old password, checks the validity of the new password, and then updates the password in the UserService if everything is valid.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * AboutEndpoint provides API endpoints for accessing and modifying user profile information, as well as changing the password for the current user.
 * The userInfo() method is used to retrieve and return information about a specific user's profile, including work experiences, interests, and language information.
 * The changePassword() method allows users to change their password by providing their old password, new password, and confirmation password.
 * If the password change is successful, an HTTP 200 OK response is returned. Otherwise, an HTTP 400 Bad Request response is returned, indicating invalid data.
 * The controller communicates with various service interfaces, such as WorkExperiencesService, InterestsService, LanguageService, and UserService,
 * to perform the necessary operations and access the required data from the underlying database.
 * </p>
 */
@RestController
@RequestMapping("/users/about/profile")
@RequiredArgsConstructor
@Slf4j
public class AboutEndpoint {

    private final WorkExperiencesService workExperiencesService;
    private final InterestsService interestsService;
    private final LanguageService languageService;
    private final UserService userService;

    /**
     * Retrieves information about a user's profile.
     *
     * <p>This method handles GET requests and returns a ResponseEntity containing an AboutUserDto object,
     * which represents a subset of the user's profile information. The AboutUserDto object includes lists
     * of languages, interests, and work experiences associated with the user.
     *
     * @return ResponseEntity containing an AboutUserDto object with the user's profile information.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<AboutUserDto> userInfo(@PathVariable("userId") User user) {
        return ResponseEntity.ok(AboutUserDto.builder()
                .languageList(languageService.findAllByUserId(user.getId()))
                .interestList(interestsService.findAllByUserId(user.getId()))
                .workExperiencesList(workExperiencesService.findAllByUserId(user.getId()))
                .build());
    }


    /**
     * Handles POST requests for changing the user's password.
     *
     * <p>This method allows the user to change their password by providing the old password, new password,
     * and password confirmation. The user's password is updated if the provided old password matches the
     * current password and the new password matches the password confirmation.
     *
     * @param oldPass     The user's current password.
     * @param newPass     The new password to be set.
     * @param confPass    The confirmation of the new password.
     * @param currentUser The authenticated user making the password change request.
     * @return ResponseEntity with HTTP status 200 if the password change is successful,
     * ResponseEntity with HTTP status 400 if the request is invalid.
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam("oldPass") String oldPass,
                                            @RequestParam("newPass") String newPass,
                                            @RequestParam("confPass") String confPass,
                                            @AuthenticationPrincipal CurrentUser currentUser) {
        if (userService.changePassword(oldPass, newPass, confPass, currentUser.getUser())) {
            return ResponseEntity.ok().build();
        }
        log.error("wrong data", new ChangePasswordException("wrong data"));
        return ResponseEntity.badRequest().build();
    }
}
