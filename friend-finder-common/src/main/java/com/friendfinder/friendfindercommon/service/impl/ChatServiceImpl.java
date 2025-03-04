package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.Chat;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.repository.ChatRepository;
import com.friendfinder.friendfindercommon.service.ChatService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * ChatServiceImpl is the implementation of the ChatService interface, which provides methods to interact with
 * the ChatRepository and perform operations related to chat messages between users.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>chatRepository: The ChatRepository interface, which allows this service to interact with the database
 *     to perform CRUD operations on the Chat entity.</li>
 *     <li>userService: The UserService interface, used to retrieve user information when creating chat messages
 *     between users.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>findAllByCurrentUserId(currentUserId): Retrieves a list of Chat objects based on the provided current
 *     user's ID. These chats represent conversations initiated by the current user.</li>
 *     <li>findAllByAnotherUserId(anotherUserId): Retrieves a list of Chat objects based on the provided another
 *     user's ID. These chats represent conversations initiated by another user who is communicating with the
 *     current user.</li>
 *     <li>findByCurrentUserIdAndAnotherUserId(firstId, secondId): Retrieves an optional Chat object based on the
 *     provided IDs of the current user and another user. It is used to check if a chat conversation between two
 *     specific users already exists.</li>
 *     <li>findById(id): Retrieves an optional Chat object based on the provided chat ID.</li>
 *     <li>save(chat): Saves the provided Chat object to the database.</li>
 *     <li>create(userId, user): Creates a new chat conversation between the current user and another user specified
 *     by their ID. It performs validation to ensure that the chat doesn't already exist, and the provided user ID
 *     is valid. If the chat is successfully created, it returns true; otherwise, it returns false.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * ChatServiceImpl is used to manage chat-related operations, such as retrieving chat conversations for specific users,
 * creating new chat instances, and saving chat messages to the database. This service is typically used by the
 * application's chat-related endpoints or controllers to handle chat interactions between users.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    @Override
    public List<Chat> findAllByCurrentUserId(int currentUserId) {
        return chatRepository.findAllByCurrentUserId(currentUserId);
    }

    @Override
    public List<Chat> findAllByAnotherUserId(int anotherUserId) {
        return chatRepository.findAllByAnotherUserId(anotherUserId);
    }

    @Override
    public Optional<Chat> findByCurrentUserIdAndAnotherUserId(int firstId, int secondId) {
        return chatRepository.findByCurrentUserIdAndAnotherUserId(firstId, secondId);
    }

    @Override
    public Optional<Chat> findById(int id) {
        return chatRepository.findById(id);
    }

    @Override
    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    @Override
    public boolean create(int userId, User user) {
        if (userId == user.getId()) {
            return false;
        }

        Optional<User> userById = userService.findUserById(userId);
        if (userById.isEmpty()) {
            return false;
        }

        Optional<Chat> byCurrentUserIdAndAnotherUserId = findByCurrentUserIdAndAnotherUserId(user.getId(), userId);
        if (byCurrentUserIdAndAnotherUserId.isPresent()) {
            return false;
        }

        Optional<Chat> byAnotherUserIdAndCurrentUserID = findByCurrentUserIdAndAnotherUserId(userId, user.getId());
        if (byAnotherUserIdAndCurrentUserID.isPresent()) {
            return false;
        }

        Chat newChat = Chat.builder()
                .anotherUser(userById.get())
                .currentUser(user)
                .build();

        save(newChat);
        return true;
    }
}

