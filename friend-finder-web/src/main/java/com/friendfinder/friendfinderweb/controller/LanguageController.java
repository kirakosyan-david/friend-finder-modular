package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.entity.Language;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.LanguageService;
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
@RequestMapping("/language")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;
    private final UserActivityService userActivityService;

    @GetMapping
    public String addLanguagePage(@AuthenticationPrincipal CurrentUser currentUser,
                                  ModelMap map
    ) {
        map.addAttribute("user", currentUser.getUser());
        map.addAttribute("userActivity", userActivityService.getAllByUserId(currentUser.getUser().getId()));

        return "edit-profile-language";
    }

    @PostMapping
    public String addLanguage(@RequestParam("language") String lang,
                              @AuthenticationPrincipal CurrentUser current
    ) {
        Language language = Language.builder()
                .language(lang)
                .user(current.getUser())
                .build();
        languageService.save(language);
        return "redirect:/language";
    }
}
