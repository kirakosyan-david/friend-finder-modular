package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.entity.Chat;
import com.friendfinder.friendfindercommon.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    List<Chat> findAllByCurrentUserId(int currentUserId);

    List<Chat> findAllByAnotherUserId(int anotherUserId);

    Optional<Chat> findByCurrentUserIdAndAnotherUserId(int firstId, int secondId);

    Optional<Chat> findById(int id);

    void save(Chat chat);

    boolean create(int userId, User user);
}

