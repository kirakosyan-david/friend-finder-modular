package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.exception.custom.DeleteFriendNotFoundException;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API endpoint for managing user friends.
 *
 * <p>This class provides endpoints for retrieving a user's friends in paginated form and for deleting friends.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
@Slf4j
public class FriendEndpoint {

    private final FriendRequestService friendRequestService;

    /**
     * Retrieves a list of user's friends by page number.
     *
     * @param currentPage The current page number.
     * @param currentUser The currently authenticated user.
     * @return ResponseEntity with a list of user's friends for the requested page.
     */
    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<List<User>> listByPage(@PathVariable("pageNumber") int currentPage,
                                                 @AuthenticationPrincipal CurrentUser currentUser) {
        Page<User> page = friendRequestService.userFriendsPageByUserId(currentUser.getUser().getId(), currentPage);
        List<User> content = page.getContent();

        return ResponseEntity.ok(content);
    }

    /**
     * Deletes a friend relationship between two users.
     *
     * @param sender   The user who sent the friend request.
     * @param receiver The user who received the friend request.
     * @return ResponseEntity with no content if the friend relationship is successfully deleted, or not found if the friend relationship does not exist.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFromFriends(@RequestParam("sender") User sender,
                                               @RequestParam("receiver") User receiver) {
        if (friendRequestService.delete(sender, receiver)) {
            return ResponseEntity.noContent().build();
        }
        log.error("friend not found, class: FriendEndpoint, method: deleteFromFriends", new DeleteFriendNotFoundException("friend not found"));
        return ResponseEntity.notFound().build();
    }
}