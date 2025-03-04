package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.exception.custom.RejectFriendRequestException;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API endpoint for managing friend requests between users.
 *
 * <p>This class provides endpoints for sending friend requests, accepting friend requests, and rejecting friend requests.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class FriendRequestEndpoint {

    private final FriendRequestService friendRequestService;

    /**
     * Sends a friend request from the sender to the receiver.
     *
     * @param sender   The user who sends the friend request.
     * @param receiver The user who receives the friend request.
     * @return ResponseEntity with the saved friend request entity.
     */
    @PostMapping("/send-request")
    public ResponseEntity<FriendRequest> sendRequest(@RequestParam("sender") User sender,
                                                     @RequestParam("receiver") User receiver) {
        return ResponseEntity.ok(friendRequestService.save(FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build()));
    }

    /**
     * Accepts a friend request from the sender by the receiver.
     *
     * @param sender   The user who sent the friend request.
     * @param receiver The user who received the friend request.
     * @return ResponseEntity with the updated friend request entity after changing its status to "ACCEPTED".
     */
    @PostMapping("/access-request")
    public ResponseEntity<FriendRequest> accessRequest(@RequestParam("sender") User sender,
                                                       @RequestParam("receiver") User receiver) {
        FriendRequest bySenderIdAndReceiverId = friendRequestService.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
        return ResponseEntity.ok(friendRequestService.changeStatus(bySenderIdAndReceiverId));
    }

    /**
     * Rejects a friend request from the sender by the receiver.
     *
     * @param sender   The user who sent the friend request.
     * @param receiver The user who received the friend request.
     * @return ResponseEntity with no content if the friend request is successfully rejected, or not found if the friend request does not exist.
     */
    @PostMapping("/reject-request")
    public ResponseEntity<String> rejectRequest(@RequestParam("sender") User sender,
                                           @RequestParam("receiver") User receiver) {

        FriendRequest bySenderIdAndReceiverId = friendRequestService.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
        if (bySenderIdAndReceiverId == null) {
            log.error("not found users", new RejectFriendRequestException());
            return ResponseEntity.notFound().build();
        }
        if (friendRequestService.delete(bySenderIdAndReceiverId)) {
            return ResponseEntity.noContent().build();
        }
        log.error("not found users", new RejectFriendRequestException());
        return ResponseEntity.notFound().build();
    }
}