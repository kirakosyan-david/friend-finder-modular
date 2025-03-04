package com.friendfinder.friendfinderrest.endpoint;


import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The UserFriendProfileEndpoint class defines RESTful endpoints related to user profiles and friend requests.
 * It provides methods to view user profiles and send friend requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/friend/profile")
public class UserFriendProfileEndpoint {

    private final FriendRequestService friendRequestService;
    private final UserService userService;

    /**
     * Endpoint to retrieve a paginated list of friends for a specific user.
     *
     * @param user        The User object representing the user whose friends to retrieve.
     * @param currentPage The page number of the friends list to retrieve.
     * @return ResponseEntity containing a list of User objects representing the user's friends.
     */
    @GetMapping("/{userId}/page/{pageNumber}")
    public ResponseEntity<List<User>> listByPage(@PathVariable("userId") User user,
                                                 @PathVariable("pageNumber") int currentPage) {
        Page<User> page = friendRequestService.userFriendsPageByUserId(user.getId(), currentPage);
        List<User> content = page.getContent();
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
}
