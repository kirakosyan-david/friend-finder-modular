package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserImage;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.UserImageService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The UserImageProfileEndpoint class defines RESTful endpoints related to user profile images and friend requests.
 * It provides methods to view user images, send friend requests, and delete user images.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/image/profile")
public class UserImageProfileEndpoint {


    private final UserImageService userImageService;
    private final FriendRequestService friendRequestService;
    private final UserService userService;

    /**
     * Endpoint to retrieve a paginated list of user images for a specific user.
     *
     * @param user        The User object representing the user whose images to retrieve.
     * @param currentPage The page number of the images list to retrieve.
     * @return ResponseEntity containing a list of UserImage objects representing the user's images.
     */
    @GetMapping("/{userId}/page/{pageNumber}")
    public ResponseEntity<List<UserImage>> listByPage(@PathVariable("userId") User user,
                                                      @PathVariable("pageNumber") int currentPage) {
        Page<UserImage> page = userImageService.userImagePageByUserId(user.getId(), currentPage);
        List<UserImage> content = page.getContent();
        return ResponseEntity.ok(content);
    }

    /**
     * Endpoint to send a friend request from one user to another.
     *
     * @param sender   The ID of the user sending the friend request.
     * @param receiver The ID of the user receiving the friend request.
     * @return ResponseEntity containing the FriendRequest object representing the sent friend request.
     */
    @GetMapping("/send-request")
    public ResponseEntity<FriendRequest> sendRequest(@RequestParam int sender,
                                                     @RequestParam int receiver) {
        Optional<User> senderId = userService.findUserById(sender);
        Optional<User> receiverId = userService.findUserById(receiver);
        FriendRequest friendRequest = new FriendRequest();
        senderId.ifPresent(friendRequest::setSender);
        receiverId.ifPresent(friendRequest::setReceiver);
        friendRequest.setStatus(FriendStatus.PENDING);
        return ResponseEntity.ok(friendRequestService.save(friendRequest));
    }

    /**
     * Endpoint to delete a user image by its ID.
     *
     * @param id The ID of the user image to be deleted.
     * @return ResponseEntity containing the deleted UserImage object.
     */
    @GetMapping("/delete/{imageId}")
    public ResponseEntity<UserImage> deleteImageById(@PathVariable("imageId") int id) {
        return ResponseEntity.ok(userImageService.deleteUserImageById(id));
    }
}