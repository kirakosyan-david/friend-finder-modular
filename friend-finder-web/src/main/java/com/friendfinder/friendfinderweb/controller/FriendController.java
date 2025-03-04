package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendRequestService friendRequestService;
    private final UserService userService;

    @GetMapping
    public String friendsPage(ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser) {
        return listByPage(1, modelMap, currentUser);
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber") int currentPage, ModelMap modelMap,
                             @ModelAttribute CurrentUser currentUser) {
        Page<User> page = friendRequestService.userFriendsPageByUserId(currentUser.getUser().getId(), currentPage);
        List<User> content = page.getContent();
        long totalItems = page.getTotalElements();
        long totalPages = page.getTotalPages();

        modelMap.addAttribute("currentPage", currentPage);
        modelMap.addAttribute("totalItems", totalItems);
        modelMap.addAttribute("totalPages", totalPages);
        modelMap.addAttribute("friends", content);

        modelMap.addAttribute("friendsCount", friendRequestService.findFriendsByUserIdCount(currentUser.getUser().getId()));
        modelMap.addAttribute("users", userService.userForAddFriend(currentUser));
        modelMap.addAttribute("requestSenders", friendRequestService.findSenderByReceiverId(currentUser.getUser().getId()));
        modelMap.addAttribute("user", currentUser.getUser());
        modelMap.addAttribute("allExceptCurrentUser", userService.findAllExceptCurrentUser(currentUser.getUser().getId()));

        return "newsfeed-friends";
    }

    @GetMapping("/delete")
    public String deleteFromFriends(@RequestParam("sender") User sender,
                                    @RequestParam("receiver") User receiver) {
        friendRequestService.delete(sender, receiver);
        return "redirect:/friends";
    }
}