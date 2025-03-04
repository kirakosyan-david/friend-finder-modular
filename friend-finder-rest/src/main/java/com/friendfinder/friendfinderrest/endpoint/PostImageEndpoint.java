package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.commentDto.CommentRequestDto;
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

import java.util.List;

/**
 * This class defines RESTful endpoints related to posts with images, including
 * adding likes and dislikes, posting comments, and retrieving posts by page.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/images")
public class PostImageEndpoint {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeAndDislikeService likeAndDislikeService;

    /**
     * Endpoint for fetching posts with images by page number.
     *
     * @param currentPage The page number to retrieve.
     * @param currentUser The CurrentUser object representing the currently logged-in user.
     * @return ResponseEntity containing a list of posts with images for the specified page.
     */
    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<List<Post>> listByPage(@PathVariable("pageNumber") int currentPage,
                                                 @AuthenticationPrincipal CurrentUser currentUser) {
        Page<Post> page = postService.postFindPageImage(currentPage, currentUser);
        List<Post> content = page.getContent();
        return ResponseEntity.ok(content);
    }

    /**
     * Endpoint for adding a like reaction to a post with an image.
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
     * Endpoint for adding a dislike reaction to a post with an image.
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
     * Endpoint for adding a comment to a post with an image.
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
     * Endpoint for removing a comment from a post with an image.
     *
     * @param id The ID of the comment to be removed.
     * @return ResponseEntity containing the deleted Comment object if successful.
     */
    @DeleteMapping("/comment/delete")
    public ResponseEntity<Comment> removeComment(@RequestParam("id") int id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}
