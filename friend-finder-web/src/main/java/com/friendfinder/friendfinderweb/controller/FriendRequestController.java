package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @GetMapping("/send-request")
    public String sendRequest(@RequestParam("sender") User sender,
                              @RequestParam("receiver") User receiver) {
        friendRequestService.save(FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build());
        return "redirect:/posts";
    }

    @GetMapping("/access-request")
    public String accessRequest(@RequestParam("sender") User sender,
                                @RequestParam("receiver") User receiver) {
        FriendRequest bySenderIdAndReceiverId = friendRequestService.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
        friendRequestService.changeStatus(bySenderIdAndReceiverId);
        return "redirect:/posts";
    }

    @GetMapping("/reject-request")
    public String rejectRequest(@RequestParam("sender") User sender,
                                @RequestParam("receiver") User receiver) {
        FriendRequest bySenderIdAndReceiverId = friendRequestService.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
        friendRequestService.delete(bySenderIdAndReceiverId);
        return "redirect:/posts";
    }
}