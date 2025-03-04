package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class AdviceController {
    @ModelAttribute("currentUser")
    public User currentUser(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser != null) {
            return currentUser.getUser();
        } else {
            return null;
        }
    }

}
