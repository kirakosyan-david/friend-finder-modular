package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {

    Optional<PostLike> findByUserIdAndPostId(int userId, int postId);
}
