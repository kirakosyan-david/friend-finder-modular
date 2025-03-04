package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class InputEndpoint {

    public final MainService mainService;

    @GetMapping("/")
    public String mainPage(ModelMap modelMap,
                           @AuthenticationPrincipal CurrentUser currentUser) {

        if (currentUser != null) {
            modelMap.addAttribute("user", currentUser.getUser());
            return "newsfeed";
        }

        modelMap.addAttribute("countries", mainService.findAllCountries());
        return "index";
    }

    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("imageName") String imageName) {
        return mainService.getImage(imageName);
    }

    @GetMapping(value = "/getVideo",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getVideo(@RequestParam("videoName") String videoName) {
        return mainService.getVideo(videoName);
    }

    @GetMapping(value = "/getProfilePic",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getProfilePic(@RequestParam("imageName") String imageName) {
        return mainService.getProfilePic(imageName);
    }

    @GetMapping(value = "/getBgProfilePic",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getBgProfilePic(@RequestParam("imageName") String imageName) {
        return mainService.getBgProfilePic(imageName);
    }

    @GetMapping("/login-register")
    public String loginPage(ModelMap modelMap) {
        modelMap.addAttribute("countries", mainService.findAllCountries());
        return "index";
    }

    @GetMapping("/successLogin")
    public String customSuccessLogin() {
        return "redirect:/posts";
    }
}
