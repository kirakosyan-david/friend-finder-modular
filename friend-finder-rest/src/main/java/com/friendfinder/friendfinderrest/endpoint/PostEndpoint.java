package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.commentDto.CommentRequestDto;
import com.friendfinder.friendfindercommon.dto.postDto.PostRequestDto;
import com.friendfinder.friendfindercommon.dto.postLikeDto.PostLikeDto;
import com.friendfinder.friendfindercommon.entity.Comment;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.entity.PostLike;
import com.friendfinder.friendfindercommon.entity.types.LikeStatus;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.CommentService;
import com.friendfinder.friendfindercommon.service.LikeAndDislikeService;
import com.friendfinder.friendfindercommon.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST API endpoint for managing posts, likes, dislikes, and comments.
 *
 * <p>This class provides various endpoints to handle posts and their interactions, such as adding posts,
 * reacting to posts with likes and dislikes, and adding/removing comments to/from posts.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostEndpoint {

    private final PostService postService;
    private final LikeAndDislikeService likeAndDislikeService;
    private final CommentService commentService;

    /**
     * Retrieves a list of posts by friends for the authenticated user.
     *
     * @param currentPage The current page number for pagination.
     * @param currentUser The currently authenticated user (obtained from the security context).
     * @return ResponseEntity with the list of posts for the specified page.
     */
    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<List<Post>> postByFriends(
            @PathVariable("pageNumber") int currentPage,
            @AuthenticationPrincipal CurrentUser currentUser) {
        Page<Post> page = postService.postFindPage(currentPage, currentUser);
        List<Post> content = page.getContent();
        return ResponseEntity.ok(content);
    }

    /**
     * Adds a new post with the specified content, image, and video.
     *
     * @param requestDto  The PostRequestDto containing the post content.
     * @param currentUser The currently authenticated user (obtained from the security context).
     * @param image       The image file attached to the post (optional).
     * @param video       The video file attached to the post (optional).
     * @return ResponseEntity with the newly created post.
     */
    @PostMapping("/add")
    public ResponseEntity<Post> postAdd(PostRequestDto requestDto,
                                        @AuthenticationPrincipal CurrentUser currentUser,
                                        @RequestParam("image") MultipartFile image,
                                        @RequestParam("video") MultipartFile video) {
        return ResponseEntity.ok(postService.postSave(requestDto, currentUser, image, video));
    }

    /**
     * Endpoint for adding a like reaction to a post.
     *
     * @param postLikeDto The PostLikeDto containing like information.
     * @param currentUser The CurrentUser object representing the currently logged-in user.
     * @param post        The Post object to which the like is added.
     * @return ResponseEntity containing the created PostLike object if successful.
     */
    @PostMapping("/reaction/like/{postId}")
    public ResponseEntity<PostLike> addLike(PostLikeDto postLikeDto,
                                            @AuthenticationPrincipal CurrentUser currentUser,
                                            @PathVariable(name = "postId") Post post) {
        postLikeDto.setLikeStatus(LikeStatus.LIKE);
        return ResponseEntity.ok(likeAndDislikeService.saveReaction(postLikeDto, currentUser, post));
    }

    /**
     * Endpoint for adding a dislike reaction to a post.
     *
     * @param postLikeDto The PostLikeDto containing dislike information.
     * @param currentUser The CurrentUser object representing the currently logged-in user.
     * @param post        The Post object to which the dislike is added.
     * @return ResponseEntity containing the created PostLike object if successful.
     */
    @PostMapping("/reaction/dislike/{postId}")
    public ResponseEntity<PostLike> addDislike(PostLikeDto postLikeDto,
                                               @AuthenticationPrincipal CurrentUser currentUser,
                                               @PathVariable(name = "postId") Post post) {
        postLikeDto.setLikeStatus(LikeStatus.DISLIKE);
        return ResponseEntity.ok(likeAndDislikeService.saveReaction(postLikeDto, currentUser, post));
    }

    /**
     * Endpoint for adding a comment to a post.
     *
     * @param comment     The CommentRequestDto containing comment information.
     * @param currentUser The CurrentUser object representing the currently logged-in user.
     * @param post        The Post object to which the comment is added.
     * @return ResponseEntity containing the created Comment object if successful.
     */
    @PostMapping("/comment/{postId}")
    public ResponseEntity<Comment> addComment(CommentRequestDto comment,
                                              @AuthenticationPrincipal CurrentUser currentUser,
                                              @PathVariable("postId") Post post) {
        return ResponseEntity.ok(commentService.addComment(comment, currentUser, post));
    }

    /**
     * Endpoint for removing a comment from a post.
     *
     * @param id The ID of the comment to be removed.
     * @return ResponseEntity containing the deleted Comment object if successful.
     */
    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<Comment> removeComment(@PathVariable("id") int id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}
