package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about-us")
public class AboutUsController {

    @GetMapping
    public String aboutUsPage(@AuthenticationPrincipal CurrentUser currentUser,
                              ModelMap map
    ) {
        map.addAttribute("user", currentUser.getUser());
        return "about-us";
    }

}
