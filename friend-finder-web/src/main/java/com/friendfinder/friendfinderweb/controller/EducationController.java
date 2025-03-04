package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.entity.Education;
import com.friendfinder.friendfindercommon.entity.WorkExperiences;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.EducationService;
import com.friendfinder.friendfindercommon.service.UserActivityService;
import com.friendfinder.friendfindercommon.service.WorkExperiencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile/work-education")
public class EducationController {

    private final EducationService educationService;
    private final WorkExperiencesService workExperiencesService;
    private final UserActivityService userActivityService;

    @GetMapping
    public String educationPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        modelMap.addAttribute("user", currentUser.getUser());
        modelMap.addAttribute("userActivity", userActivityService.getAllByUserId(currentUser.getUser().getId()));
        return "edit-profile-work-edu";
    }

    @PostMapping("/education/add")
    public String educationAdd(@ModelAttribute Education education,
                               @AuthenticationPrincipal CurrentUser currentUser) {
        educationService.saveEducation(education, currentUser);
        return "redirect:/profile/work-education";
    }

    @PostMapping("/work/add")
    public String workAdd(@ModelAttribute WorkExperiences workExperiences,
                          @AuthenticationPrincipal CurrentUser currentUser) {
        workExperiencesService.saveWorkExperiences(workExperiences, currentUser);
        return "redirect:/profile/work-education";
    }
}
