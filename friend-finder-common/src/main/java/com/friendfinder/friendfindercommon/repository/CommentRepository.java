package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
