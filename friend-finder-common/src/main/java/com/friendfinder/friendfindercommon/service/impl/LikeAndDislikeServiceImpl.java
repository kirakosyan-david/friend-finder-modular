package com.friendfinder.friendfindercommon.service.impl;


import com.friendfinder.friendfindercommon.dto.postLikeDto.PostLikeDto;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.entity.PostLike;
import com.friendfinder.friendfindercommon.entity.types.LikeStatus;
import com.friendfinder.friendfindercommon.mapper.PostLikeMapper;
import com.friendfinder.friendfindercommon.repository.PostLikeRepository;
import com.friendfinder.friendfindercommon.repository.PostRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.LikeAndDislikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <p>
 * LikeAndDislikeServiceImpl is the implementation of the LikeAndDislikeService interface, providing methods to interact
 * with the PostLikeRepository and perform operations related to post reactions (likes and dislikes).
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>postLikeRepository: The PostLikeRepository interface, allowing this service to interact with the database
 *     to perform CRUD operations on PostLike entities.</li>
 *     <li>postRepository: The PostRepository interface, enabling this service to interact with the database to update
 *     Post entities based on reaction counts.</li>
 *     <li>postLikeMapper: The PostLikeMapper interface, used for mapping PostLikeDto objects to PostLike entities.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>saveReaction(postLikeDto, currentUser, post): Saves or updates a PostLike entity in the database based on the
 *     provided PostLikeDto object. If the user has not reacted to the post, a new PostLike entity is created, and the
 *     like/dislike count of the corresponding Post is updated accordingly. If the user has already reacted, their
 *     previous reaction is removed, and the like/dislike count is adjusted accordingly.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * LikeAndDislikeServiceImpl is used to manage post reactions in the application. It is typically utilized by the
 * application's endpoints or controllers that handle user interactions related to liking and disliking posts. For example,
 * when a user likes a post, this service is responsible for creating a new PostLike entity and updating the like count
 * of the post. If the user decides to remove their like or dislike, this service handles the removal and updates the
 * corresponding counts accordingly. The service ensures that the user's reaction to a post is reflected accurately in
 * the database and that the post's like/dislike counts stay in sync with user actions.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class LikeAndDislikeServiceImpl implements LikeAndDislikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final PostLikeMapper postLikeMapper;

    @Override
    @Transactional
    public PostLike saveReaction(PostLikeDto postLikeDto, CurrentUser currentUser, Post post) {
        Optional<PostLike> byUserIdAndPostId = postLikeRepository.findByUserIdAndPostId(currentUser.getUser().getId(), post.getId());
        if (byUserIdAndPostId.isEmpty()) {
            postLikeDto.setUser(currentUser.getUser());
            postLikeDto.setPost(post);
            if (postLikeDto.getLikeStatus() == LikeStatus.LIKE) {
                post.setLikeCount(post.getLikeCount() + 1);
                postRepository.save(post);
            } else {
                post.setDislikeCount(post.getDislikeCount() + 1);
                postRepository.save(post);
            }
            PostLike postLike = postLikeMapper.map(postLikeDto);
            return postLikeRepository.save(postLike);
        } else {
            PostLike postLikeDelete = byUserIdAndPostId.get();
            postLikeRepository.delete(postLikeDelete);
            if (postLikeDelete.getLikeStatus() == LikeStatus.LIKE) {
                post.setLikeCount(post.getLikeCount() - 1);
                postRepository.save(post);
            } else {
                post.setDislikeCount(post.getDislikeCount() - 1);
                postRepository.save(post);
            }
        }
        return null;
    }
}


