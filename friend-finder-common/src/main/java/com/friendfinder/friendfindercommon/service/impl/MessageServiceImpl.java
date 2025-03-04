package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.dto.chatDto.SendMessageDto;
import com.friendfinder.friendfindercommon.entity.Chat;
import com.friendfinder.friendfindercommon.entity.Message;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.repository.MessageRepository;
import com.friendfinder.friendfindercommon.service.ChatService;
import com.friendfinder.friendfindercommon.service.MessageService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * MessageServiceImpl is a service class that provides methods to handle sending and saving chat messages between users.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>messageRepository: The MessageRepository interface used to access and save message-related data to the database.</li>
 *     <li>userService: The UserService interface used to access and retrieve user-related data from the database.</li>
 *     <li>chatService: The ChatService interface used to access and retrieve chat-related data from the database.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>save(sendMessageDto, currentUser): Saves a chat message to the database based on the information provided in the
 *     SendMessageDto object. The method validates the input data, checks if the receiver user and chat exist in the database,
 *     and then saves the message with the sender, receiver, chat, content, and timestamp.</li>
 *     <li>save(message): Saves a message object to the database using the messageRepository.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * MessageServiceImpl is used to manage chat messages in the application. It provides the capability to save messages between
 * users and store them in the database. The save() method with the SendMessageDto parameter is commonly used when users send
 * new messages in a chat conversation. It ensures that the message is properly associated with the sender, receiver, and chat,
 * and then stores the message with the current timestamp. The save() method with the Message parameter is useful when handling
 * specific message objects, such as when retrieving messages from external sources or processing them before saving to the
 * database. Overall, MessageServiceImpl enhances the chat functionality of the application, enabling smooth communication
 * between users.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;

    @Override
    public boolean save(SendMessageDto sendMessageDto, User currentUser) {
        if (sendMessageDto == null || currentUser == null) return false;

        Optional<User> userById = userService.findUserById(sendMessageDto.getReceiverId());
        if (userById.isEmpty()) {
            return false;
        }

        Optional<Chat> chatById = chatService.findById(sendMessageDto.getChatId());
        if (chatById.isEmpty()) {
            return false;
        }

        messageRepository.save(Message.builder()
                .receiver(userById.get())
                .chat(chatById.get())
                .sender(currentUser)
                .content(sendMessageDto.getContent())
                .sentAt(LocalDateTime.now())
                .build());
        return true;
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }
}
