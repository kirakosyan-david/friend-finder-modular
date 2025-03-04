package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.dto.commentDto.CommentRequestDto;
import com.friendfinder.friendfindercommon.dto.postLikeDto.PostLikeDto;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.entity.types.LikeStatus;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/images")
public class PostImageController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final LikeAndDislikeService likeAndDislikeService;
    private final FriendRequestService friendRequestService;


    @GetMapping
    public String getPostImageByFriends(ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser) {
        return listByPage(1, modelMap, currentUser);

    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber") int currentPage, ModelMap modelMap,
                             @AuthenticationPrincipal CurrentUser currentUser) {
        Page<Post> page = postService.postFindPageImage(currentPage, currentUser);
        List<Post> content = page.getContent();
        long totalItems = page.getTotalElements();
        long totalPages = page.getTotalPages();

        modelMap.addAttribute("currentPage", currentPage);
        modelMap.addAttribute("totalItems", totalItems);
        modelMap.addAttribute("totalPages", totalPages);
        modelMap.addAttribute("posts", content);
        modelMap.addAttribute("allExceptCurrentUser", userService.findAllExceptCurrentUser(currentUser.getUser().getId()));
        modelMap.addAttribute("user", currentUser.getUser());
        modelMap.addAttribute("comments", commentService.commentList());
        modelMap.addAttribute("users", userService.userForAddFriend(currentUser));
        modelMap.addAttribute("requestSenders", friendRequestService.findSenderByReceiverId(currentUser.getUser().getId()));
        return "newsfeed-images";
    }

    @PostMapping("/reaction/like/{postId}")
    public String addLike(@ModelAttribute PostLikeDto postLikeDto,
                          @AuthenticationPrincipal CurrentUser currentUser,
                          @PathVariable(name = "postId") Post post) {
        postLikeDto.setLikeStatus(LikeStatus.LIKE);
        likeAndDislikeService.saveReaction(postLikeDto, currentUser, post);
        return "redirect:/posts/images";
    }

    @PostMapping("/reaction/dislike/{postId}")
    public String addDislike(@ModelAttribute PostLikeDto postLikeDto,
                             @AuthenticationPrincipal CurrentUser currentUser,
                             @PathVariable(name = "postId") Post post) {
        postLikeDto.setLikeStatus(LikeStatus.DISLIKE);
        likeAndDislikeService.saveReaction(postLikeDto, currentUser, post);
        return "redirect:/posts/images";
    }

    @PostMapping("/comment/{postId}")
    public String addComment(@ModelAttribute CommentRequestDto comment,
                             @AuthenticationPrincipal CurrentUser currentUser,
                             @PathVariable("postId") Post post) {
        commentService.addComment(comment, currentUser, post);
        return "redirect:/posts/images";
    }

    @GetMapping("/comment/delete")
    public String removeComment(@RequestParam("id") int id) {
        commentService.deleteComment(id);
        return "redirect:/posts/images";
    }
}
