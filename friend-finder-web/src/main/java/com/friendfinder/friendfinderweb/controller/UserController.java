package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.dto.userDto.UserRegisterRequestDto;
import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${site.url}")
    String siteUrl;

    @GetMapping("/register")
    public String registerPage(ModelMap modelMap) {
        List<Country> allCountries = userService.findAllCountries();
        modelMap.addAttribute("countries", allCountries);

        return "index";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterRequestDto userDto) {
        userService.userRegister(userDto);
        return "redirect:/";
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam String email,
                             @RequestParam String token) {
        Optional<User> byEmail = userService.findByEmail(email);
        if (byEmail.isEmpty()) {
            return "redirect:/";
        }
        if (byEmail.get().isEnabled()) {
            return "redirect:/";
        }
        if (byEmail.get().getToken().equals(token)) {
            User user = byEmail.get();
            user.setEnabled(true);
            user.setToken(null);
            userService.save(user);
        }
        return "redirect:/";
    }

    @GetMapping
    public String sendDataToHeader(@AuthenticationPrincipal CurrentUser currentUser, ModelMap map) {
        map.addAttribute("user", currentUser.getUser());
        return "fragment/header-menu-fragment";
    }
}
