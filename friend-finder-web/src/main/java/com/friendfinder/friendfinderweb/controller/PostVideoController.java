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
@RequestMapping("/posts/video")
public class PostVideoController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final LikeAndDislikeService likeAndDislikeService;
    private final FriendRequestService friendRequestService;


    @GetMapping
    public String getPostVideoByFriends(ModelMap modelMap,
                                        @AuthenticationPrincipal CurrentUser currentUser) {
        return listByPageVideo(1, modelMap, currentUser);
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPageVideo(@PathVariable("pageNumber") int currentPage, ModelMap modelMap,
                                  @AuthenticationPrincipal CurrentUser currentUser) {
        Page<Post> page = postService.postFindPageVideo(currentPage, currentUser);
        List<Post> content = page.getContent();
        long totalItems = page.getTotalElements();
        long totalPages = page.getTotalPages();

        modelMap.addAttribute("currentPage", currentPage);
        modelMap.addAttribute("totalItems", totalItems);
        modelMap.addAttribute("totalPages", totalPages);
        modelMap.addAttribute("posts", content);

        modelMap.addAttribute("user", currentUser.getUser());
        modelMap.addAttribute("comments", commentService.commentList());
        modelMap.addAttribute("users", userService.userForAddFriend(currentUser));
        modelMap.addAttribute("allExceptCurrentUser", userService.findAllExceptCurrentUser(currentUser.getUser().getId()));
        modelMap.addAttribute("requestSenders", friendRequestService.findSenderByReceiverId(currentUser.getUser().getId()));

        return "newsfeed-videos";
    }

    @PostMapping("/reaction/like/{postId}")
    public String addLike(@ModelAttribute PostLikeDto postLikeDto,
                          @AuthenticationPrincipal CurrentUser currentUser,
                          @PathVariable(name = "postId") Post post) {
        postLikeDto.setLikeStatus(LikeStatus.LIKE);
        likeAndDislikeService.saveReaction(postLikeDto, currentUser, post);
        return "redirect:/posts/video";
    }

    @PostMapping("/reaction/dislike/{postId}")
    public String addDislike(@ModelAttribute PostLikeDto postLikeDto,
                             @AuthenticationPrincipal CurrentUser currentUser,
                             @PathVariable(name = "postId") Post post) {
        postLikeDto.setLikeStatus(LikeStatus.DISLIKE);
        likeAndDislikeService.saveReaction(postLikeDto, currentUser, post);
        return "redirect:/posts/video";
    }

    @PostMapping("/comment/{postId}")
    public String addComment(@ModelAttribute CommentRequestDto comment,
                             @AuthenticationPrincipal CurrentUser currentUser,
                             @PathVariable("postId") Post post) {
        commentService.addComment(comment, currentUser, post);
        return "redirect:/posts/video";
    }

    @GetMapping("/comment/delete")
    public String removeComment(@RequestParam("id") int id) {
        commentService.deleteComment(id);
        return "redirect:/posts/video";
    }
}
