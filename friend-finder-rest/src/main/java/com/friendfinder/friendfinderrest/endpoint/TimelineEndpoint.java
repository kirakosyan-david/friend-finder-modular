package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.userDto.UserUpdateRequestDto;
import com.friendfinder.friendfindercommon.dto.userDto.UserUpdateResponseDto;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserImage;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.impl.TimelineServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * The TimelineEndpoint class defines RESTful endpoints for managing user timelines.
 * It provides methods to edit basic user information, change profile pictures, and update profile background pictures.
 */
@RestController
@RequestMapping("/timeline")
@RequiredArgsConstructor
public class TimelineEndpoint {

    private final TimelineServiceImpl timelineService;

    /**
     * Endpoint for editing basic user information.
     *
     * @param userUpdateRequestDto The DTO containing updated user information.
     * @param currentUser          The CurrentUser object representing the currently logged-in user.
     * @return ResponseEntity containing the response DTO with updated user information.
     */
    @PutMapping("/edit-basic")
    public ResponseEntity<UserUpdateResponseDto> editBasic(@RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                                           @AuthenticationPrincipal CurrentUser currentUser) {
        User user = timelineService.updateUser(userUpdateRequestDto, currentUser);
        return ResponseEntity.ok(
                UserUpdateResponseDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .surname(user.getSurname())
                        .email(user.getEmail())
                        .country(user.getCountry())
                        .dateOfBirth(user.getDateOfBirth())
                        .gender(user.getGender())
                        .personalInformation(user.getPersonalInformation())
                        .city(user.getCity())
                        .build()
        );
    }

    /**
     * Endpoint for changing the user's profile picture.
     *
     * @param profilePic  The MultipartFile containing the new profile picture.
     * @param currentUser The CurrentUser object representing the currently logged-in user.
     * @return ResponseEntity with a success message after changing the profile picture.
     */
    @PostMapping("/change-profile-pic")
    public ResponseEntity<String> changeProfilePic(@RequestParam MultipartFile profilePic,
                                                   @AuthenticationPrincipal CurrentUser currentUser) {
        UserImage userImage = UserImage.builder()
                .imageName(profilePic.getName())
                .user(currentUser.getUser())
                .build();

        User user = timelineService.updateUserProfilePic(profilePic, currentUser, userImage);

        return ResponseEntity.ok("the user with id {" + user.getId() + "} has changed the profile picture");
    }

    /**
     * Endpoint for changing the user's profile background picture.
     *
     * @param image       The MultipartFile containing the new profile background picture.
     * @param currentUser The CurrentUser object representing the currently logged-in user.
     * @return ResponseEntity with a success message after changing the profile background picture.
     */
    @PostMapping("/change-profile-bg-pic")
    public ResponseEntity<String> changeProfileBackgroundPic(@RequestParam MultipartFile image,
                                                             @AuthenticationPrincipal CurrentUser currentUser) {
        User user = timelineService.updateUserProfileBackgroundPic(image, currentUser);
        return ResponseEntity.ok("the user with id {" + user.getId() + "} has changed the profile background picture");
    }
}
