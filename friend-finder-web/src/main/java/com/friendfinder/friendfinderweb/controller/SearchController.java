package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.SearchService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search/friend")
public class SearchController {

    private final SearchService searchService;
    private final UserService userService;
    private final FriendRequestService friendRequestService;


    @PostMapping("/{pageNumber}")
    public String listByPageSearch(@RequestParam String keyword,
                                   @PathVariable("pageNumber") int currentPage, ModelMap modelMap,
                                   @AuthenticationPrincipal CurrentUser currentUser) {
        Page<User> page = searchService.searchByKeyword(keyword, currentUser, currentPage);

        long totalItems = page.getTotalElements();
        long totalPages = page.getTotalPages();

        modelMap.addAttribute("currentPage", currentPage);
        modelMap.addAttribute("totalItems", totalItems);
        modelMap.addAttribute("totalPages", totalPages);
        modelMap.addAttribute("result", page.getContent());
        modelMap.addAttribute("requestSenders", friendRequestService.findSenderByReceiverId(currentUser.getUser().getId()));
        modelMap.addAttribute("user", currentUser.getUser());
        modelMap.addAttribute("users", userService.userForAddFriend(currentUser));
        modelMap.addAttribute("allExceptCurrentUser", userService.findAllExceptCurrentUser(currentUser.getUser().getId()));
        return "resultOfSearchUsers";
    }
}
