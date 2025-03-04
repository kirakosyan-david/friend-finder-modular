package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.InterestsService;
import com.friendfinder.friendfindercommon.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/interests")
public class InterestController {

    private final InterestsService interestsService;
    private final UserActivityService userActivityService;

    @GetMapping
    public String interestPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        modelMap.addAttribute("user", currentUser.getUser());
        modelMap.addAttribute("interests", interestsService.findAllByUserId(currentUser.getUser().getId()));
        modelMap.addAttribute("userActivity", userActivityService.getAllByUserId(currentUser.getUser().getId()));
        return "edit-profile-interests";
    }

    @PostMapping
    public String interestsAdd(@RequestParam("interest") String interest,
                               @AuthenticationPrincipal CurrentUser currentUser) {
        interestsService.interestSave(interest, currentUser);
        return "redirect:/interests";
    }
}
