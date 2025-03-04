package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.CommentService;
import com.friendfinder.friendfindercommon.service.PostService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/users")
    public String adminUsersPage(@AuthenticationPrincipal CurrentUser currentUser,
                                 ModelMap map
    ) {
        User user = currentUser.getUser();
        map.addAttribute("user", user);
        map.addAttribute("users", userService.userFindAll());
        return "admin-users";
    }

    @GetMapping("/posts")
    public String adminPostsPage(@AuthenticationPrincipal CurrentUser currentUser,
                                 ModelMap map
    ) {
        User user = currentUser.getUser();
        map.addAttribute("user", user);
        map.addAttribute("posts", postService.findAll());
        return "admin-posts";
    }

    @GetMapping("/comments")
    public String adminCommentsPage(@AuthenticationPrincipal CurrentUser currentUser,
                                    ModelMap map
    ) {
        User user = currentUser.getUser();
        map.addAttribute("user", user);
        map.addAttribute("comments", commentService.commentList());
        return "admin-comments";
    }

    @GetMapping("/users/delete/{id}")
    @Transactional
    public String deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/posts/delete/{id}")
    @Transactional
    public String deletePostById(@PathVariable("id") int id) {
        postService.deletePostId(id);
        return "redirect:/admin/posts";
    }

    @GetMapping("/comments/delete/{id}")
    @Transactional
    public String deleteCommentById(@PathVariable("id") int id) {
        commentService.deleteComment(id);
        return "redirect:/admin/comments";
    }

    @GetMapping("/users/block/{id}")
    @Transactional
    public String blockUserById(@PathVariable("id") int id) {
        userService.blockUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/unblock/{id}")
    @Transactional
    public String unblockUserById(@PathVariable("id") int id) {
        userService.unblockUserById(id);
        return "redirect:/admin/users";
    }
}
