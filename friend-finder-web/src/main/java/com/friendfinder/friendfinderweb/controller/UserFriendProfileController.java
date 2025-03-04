package com.friendfinder.friendfinderweb.controller;


import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/friend/profile")
public class UserFriendProfileController {

    private final FriendRequestService friendRequestService;
    private final UserActivityService userActivityService;

    @GetMapping("/{userId}")
    public String friendsPage(@PathVariable("userId") User user, ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser) {

        return listByPage(user, 1, modelMap, currentUser);
    }

    @GetMapping("/{userId}/page/{pageNumber}")
    public String listByPage(@PathVariable("userId") User user, @PathVariable("pageNumber") int currentPage, ModelMap modelMap,
                             @ModelAttribute CurrentUser currentUser) {
        Page<User> page = friendRequestService.userFriendsPageByUserId(user.getId(), currentPage);
        List<User> content = page.getContent();
        long totalItems = page.getTotalElements();
        long totalPages = page.getTotalPages();

        modelMap.addAttribute("currentPage", currentPage);
        modelMap.addAttribute("totalItems", totalItems);
        modelMap.addAttribute("totalPages", totalPages);
        modelMap.addAttribute("friends", content);

        modelMap.addAttribute("profile", currentUser.getUser());
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("friendsCount", friendRequestService.findFriendsByUserIdCount(user.getId()));
        modelMap.addAttribute("userActivity", userActivityService.getAllByUserId(user.getId()));
        return "timeline-friends";
    }


    @GetMapping("/send-request")
    public String sendRequest(@RequestParam("sender") User sender,
                              @RequestParam("receiver") User receiver) {
        friendRequestService.save(FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build());
        return "redirect:/users/friend/profile/" + receiver.getId();
    }
}
