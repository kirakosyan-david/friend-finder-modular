package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.commentDto.CommentRequestDto;
import com.friendfinder.friendfindercommon.dto.postDto.PostRequestDto;
import com.friendfinder.friendfindercommon.dto.postLikeDto.PostLikeDto;
import com.friendfinder.friendfindercommon.entity.*;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.entity.types.LikeStatus;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * The UserProfileEndpoint class defines RESTful endpoints related to user profiles.
 * It provides methods to view user posts, add posts, send friend requests,
 * delete posts, add likes and dislikes, add comments, and remove comments.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/profile")
public class UserProfileEndpoint {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeAndDislikeService likeAndDislikeService;
    private final FriendRequestService friendRequestService;
    private final UserService userService;

    /**
     * Endpoint to retrieve a paginated list of posts for a specific user.
     *
     * @param user        The User object representing the user whose posts to retrieve.
     * @param currentPage The page number of the posts list to retrieve.
     * @return ResponseEntity containing a list of Post objects representing the user's posts.
     */
    @GetMapping("/{userId}/page/{pageNumber}")
    public ResponseEntity<List<Post>> listByPage(@PathVariable("userId") User user,
                                                 @PathVariable("pageNumber") int currentPage) {
        Page<Post> page = postService.postPageByUserId(user.getId(), currentPage);
        List<Post> content = page.getContent();
        ResponseEntity.ok(content);
        return ResponseEntity.ok(content);
    }

    /**
     * Endpoint to add a new post for the current user.
     *
     * @param requestDto  The PostRequestDto object containing the post data.
     * @param currentUser The CurrentUser object representing the currently authenticated user.
     * @param image       The image file attached to the post (optional).
     * @param video       The video file attached to the post (optional).
     * @return ResponseEntity containing the newly created Post object.
     */
    @PostMapping("/add")
    public ResponseEntity<Post> postAdd(PostRequestDto requestDto,
                                        @AuthenticationPrincipal CurrentUser currentUser,
                                        @RequestParam MultipartFile image,
                                        @RequestParam MultipartFile video) {
        return ResponseEntity.ok(postService.postSave(requestDto, currentUser, image, video));
    }

    /**
     * Endpoint to send a friend request from one user to another.
     *
     * @param sender   The ID of the user sending the friend request.
     * @param receiver The ID of the user receiving the friend request.
     * @return ResponseEntity containing the FriendRequest object representing the sent friend request.
     */
    @GetMapping("/send-request")
    public ResponseEntity<FriendRequest> sendRequest(@RequestParam int sender,
                                                     @RequestParam int receiver) {
        Optional<User> senderId = userService.findUserById(sender);
        Optional<User> receiverId = userService.findUserById(receiver);
        FriendRequest friendRequest = new FriendRequest();
        senderId.ifPresent(friendRequest::setSender);
        receiverId.ifPresent(friendRequest::setReceiver);
        friendRequest.setStatus(FriendStatus.PENDING);
        return ResponseEntity.ok(friendRequestService.save(friendRequest));
    }

    /**
     * Endpoint to delete a post by its ID.
     *
     * @param id The ID of the post to be deleted.
     * @return ResponseEntity containing the deleted Post object.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Post> deletePostById(@PathVariable("id") int id) {
        return ResponseEntity.ok(postService.deletePostId(id));

    }

    /**
     * Endpoint to add a like to a post.
     *
     * @param postLikeDto The PostLikeDto object containing the like data.
     * @param currentUser The CurrentUser object representing the currently authenticated user.
     * @param post        The Post object representing the post to like.
     * @return ResponseEntity containing the PostLike object representing the like.
     */
    @PostMapping("/reaction/like/{postId}")
    public ResponseEntity<PostLike> addLike(PostLikeDto postLikeDto,
                                            @AuthenticationPrincipal CurrentUser currentUser,
                                            @PathVariable(name = "postId") Post post) {
        postLikeDto.setLikeStatus(LikeStatus.LIKE);
        return ResponseEntity.ok(likeAndDislikeService.saveReaction(postLikeDto, currentUser, post));
    }

    /**
     * Endpoint to add a dislike to a post.
     *
     * @param postLikeDto The PostLikeDto object containing the dislike data.
     * @param currentUser The CurrentUser object representing the currently authenticated user.
     * @param post        The Post object representing the post to dislike.
     * @return ResponseEntity containing the PostLike object representing the dislike.
     */
    @PostMapping("/reaction/dislike/{postId}")
    public ResponseEntity<PostLike> addDislike(PostLikeDto postLikeDto,
                                               @AuthenticationPrincipal CurrentUser currentUser,
                                               @PathVariable(name = "postId") Post post) {
        postLikeDto.setLikeStatus(LikeStatus.DISLIKE);
        return ResponseEntity.ok(likeAndDislikeService.saveReaction(postLikeDto, currentUser, post));
    }

    /**
     * Endpoint to add a comment to a post.
     *
     * @param comment     The CommentRequestDto object containing the comment data.
     * @param currentUser The CurrentUser object representing the currently authenticated user.
     * @param post        The Post object representing the post to comment on.
     * @return ResponseEntity containing the Comment object representing the added comment.
     */
    @PostMapping("/comment/{postId}")
    public ResponseEntity<Comment> addComment(CommentRequestDto comment,
                                              @AuthenticationPrincipal CurrentUser currentUser,
                                              @PathVariable("postId") Post post) {
        return ResponseEntity.ok(commentService.addComment(comment, currentUser, post));
    }

    /**
     * Endpoint to remove a comment by its ID.
     *
     * @param id The ID of the comment to be removed.
     * @return ResponseEntity containing the deleted Comment object.
     */
    @DeleteMapping("/comment/delete{id}")
    public ResponseEntity<Comment> removeComment(@PathVariable("id") int id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}
