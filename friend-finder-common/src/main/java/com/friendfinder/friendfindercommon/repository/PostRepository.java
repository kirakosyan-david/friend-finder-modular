package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUserId(int id);

    Page<Post> findByUserIdIn(List<Integer> userIds, Pageable pageable);
    Page<Post> findPostsByMusicFileNameIn(List<String> musicFileName, Pageable pageable);
    Page<Post> findPostsByImgNameIn(List<String> imgName, Pageable pageable);

    Page<Post> findPostsByUserIdIn(List<Integer> userId, Pageable pageable);
}
