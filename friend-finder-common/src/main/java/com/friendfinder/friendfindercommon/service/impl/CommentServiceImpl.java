package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.dto.commentDto.CommentRequestDto;
import com.friendfinder.friendfindercommon.entity.Comment;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.mapper.CommentMapper;
import com.friendfinder.friendfindercommon.repository.CommentRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.CommentService;
import com.friendfinder.friendfindercommon.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * CommentServiceImpl is the implementation of the CommentService interface, which provides methods to interact with
 * the CommentRepository and perform operations related to comments on posts.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>commentRepository: The CommentRepository interface, which allows this service to interact with the database
 *     to perform CRUD operations on the Comment entity.</li>
 *     <li>userActivityService: The UserActivityService interface, used to record user activity when a new comment
 *     is added to a post.</li>
 *     <li>commentMapper: The CommentMapper interface, used to map CommentRequestDto objects to Comment entities.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>commentList(): Retrieves a list of all comments in the system.</li>
 *     <li>addComment(comment, currentUser, post): Adds a new comment to a specific post. It takes a CommentRequestDto
 *     object containing the comment details, the current authenticated user (CurrentUser), and the Post to which the
 *     comment will be added. The method maps the CommentRequestDto to a Comment entity, sets the appropriate fields
 *     (user, post, commentaryText, and datetime), saves the comment to the database, and records the user activity
 *     as "commented on a post" using the userActivityService.</li>
 *     <li>deleteComment(id): Deletes a comment with the provided ID from the database, if it exists.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * CommentServiceImpl is used to manage operations related to comments on posts, such as retrieving a list of all
 * comments, adding new comments to posts, and deleting comments. This service is typically used by the application's
 * endpoints or controllers related to comment functionality.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserActivityService userActivityService;
    private final CommentMapper commentMapper;

    @Override
    public List<Comment> commentList() {
        return commentRepository.findAll();
    }

    @Override
    public Comment addComment(CommentRequestDto comment, CurrentUser currentUser, Post post) {
        Comment commentSave = commentMapper.map(CommentRequestDto.builder()
                .user(currentUser.getUser())
                .post(post)
                .commentaryText(comment.getCommentaryText())
                .datetime(LocalDateTime.now())
                .build());
        userActivityService.save(currentUser.getUser(), "commented on a post");
        return commentRepository.save(commentSave);
    }

    @Override
    public Comment deleteComment(int id) {
        Optional<Comment> byId = commentRepository.findById(id);
        if (byId.isPresent()) {
            Comment comment = byId.get();
            commentRepository.deleteById(comment.getId());
        }
        return null;
    }
}
