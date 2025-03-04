package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.entity.Interest;
import com.friendfinder.friendfindercommon.entity.Language;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.WorkExperiences;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/about/profile")
@RequiredArgsConstructor
public class AboutController {
    private final WorkExperiencesService workExperiencesService;
    private final InterestsService interestsService;
    private final LanguageService languageService;
    private final UserService userService;
    private final FriendRequestService friendRequestService;
    private final UserActivityService userActivityService;

    @GetMapping("/{userId}")
    public String userInfo(@PathVariable("userId") User user, ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser) {
        List<WorkExperiences> workExperiencesList = workExperiencesService.findAllByUserId(user.getId());
        List<Interest> interestList = interestsService.findAllByUserId(user.getId());
        List<Language> languageList = languageService.findAllByUserId(user.getId());

        modelMap.addAttribute("workExperiences", workExperiencesList);
        modelMap.addAttribute("interests", interestList);
        modelMap.addAttribute("profile", currentUser.getUser());
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("languages", languageList);
        modelMap.addAttribute("friendsCount", friendRequestService.findFriendsByUserIdCount(user.getId()));
        modelMap.addAttribute("userActivity", userActivityService.getAllByUserId(user.getId()));
        return "timeline-about";
    }


    @GetMapping("/change-password")
    public String changePasswordPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        modelMap.addAttribute("user", currentUser.getUser());
        return "edit-profile-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPass") String oldPass,
                                 @RequestParam("newPass") String newPass,
                                 @RequestParam("confPass") String confPass,
                                 @AuthenticationPrincipal CurrentUser currentUser) {
        if (userService.changePassword(oldPass, newPass, confPass, currentUser.getUser())) {
            return "redirect:/posts";
        }
        return "redirect:/users/about/profile/change-password";
    }
}
