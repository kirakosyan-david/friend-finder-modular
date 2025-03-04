package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.commentDto.CommentRequestDto;
import com.friendfinder.friendfindercommon.dto.postDto.PostResponseDto;
import com.friendfinder.friendfindercommon.dto.userDto.UserDto;
import com.friendfinder.friendfindercommon.exception.custom.WrongUserIdException;
import com.friendfinder.friendfindercommon.mapper.CommentMapper;
import com.friendfinder.friendfindercommon.mapper.PostMapper;
import com.friendfinder.friendfindercommon.mapper.UserMapper;
import com.friendfinder.friendfindercommon.service.CommentService;
import com.friendfinder.friendfindercommon.service.PostService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API endpoints for admin operations.
 *
 * <p>This class handles various administrative operations such as retrieving all users,
 * all posts, and all comments. It also provides functionality to delete users, posts,
 * and comments by their respective IDs. Additionally, it allows blocking and unblocking
 * users by their IDs.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminEndpoint {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    /**
     * Retrieves a list of all users.
     *
     * @return ResponseEntity with a list of UserDto containing user information.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userMapper.mapToDtos(userService.userFindAll()));
    }

    /**
     * Retrieves a list of all posts.
     *
     * @return ResponseEntity with a list of PostResponseDto containing post information.
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postMapper.mapResp(postService.findAll()));
    }

    /**
     * Retrieves a list of all comments.
     *
     * @return ResponseEntity with a list of CommentRequestDto containing comment information.
     */
    @GetMapping("/comments")
    public ResponseEntity<List<CommentRequestDto>> getAllComments() {
        return ResponseEntity.ok(commentMapper.mapToDtos(commentService.commentList()));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to be deleted.
     * @return ResponseEntity with a message indicating the success of the operation.
     */
    @DeleteMapping("/users/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        String body = "user with id {" + id + "} successfully deleted";
        return new ResponseEntity<>(body, HttpStatus.OK);
    }


    /**
     * Deletes a posts by their ID.
     *
     * @param id The ID of the post to be deleted.
     * @return ResponseEntity with a message indicating the success of the operation.
     */
    @DeleteMapping("/posts/delete/{id}")
    @Transactional
    public ResponseEntity<String> deletePostById(@PathVariable("id") int id) {
        postService.deletePostId(id);
        String body = "post with id {" + id + "} successfully deleted";
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * Deletes a comments by their ID.
     *
     * @param id The ID of the comment to be deleted.
     * @return ResponseEntity with a message indicating the success of the operation.
     */
    @DeleteMapping("/comments/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteCommentById(@PathVariable("id") int id) {
        commentService.deleteComment(id);
        String body = "comment with id {" + id + "} successfully deleted";
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * Blocks a user by their ID.
     *
     * @param id The ID of the user to be blocked.
     * @return ResponseEntity with a message indicating the success of the operation.
     */
    @PutMapping("/users/block/{id}")
    @Transactional
    public ResponseEntity<String> blockUserById(@PathVariable("id") int id) {
        boolean block = userService.blockUserById(id);
        if (!block) {
            log.error("wrong user id class: AdminEndpoint.java, method: blockUserById", new WrongUserIdException("wrong user id"));
            return new ResponseEntity<>("wrong user id", HttpStatus.NOT_FOUND);
        }
        String body = "user with id {" + id + "} successfully blocked";
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * Unblocks a user by their ID.
     *
     * @param id The ID of the user to be unblocked.
     * @return ResponseEntity with a message indicating the success of the operation.
     */
    @PutMapping("/users/unblock/{id}")
    @Transactional
    public ResponseEntity<String> unblockUserById(@PathVariable("id") int id) {
        boolean unblock = userService.unblockUserById(id);
        if (!unblock) {
            log.error("wrong user id class: AdminEndpoint.java, method: unblockUserById", new WrongUserIdException("wrong user id"));
            return new ResponseEntity<>("wrong user id", HttpStatus.NOT_FOUND);
        }
        String body = "user with id {" + id + "} successfully unblocked";
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
