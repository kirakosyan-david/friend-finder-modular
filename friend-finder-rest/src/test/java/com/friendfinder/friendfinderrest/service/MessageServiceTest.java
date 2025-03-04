package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.dto.chatDto.SendMessageDto;
import com.friendfinder.friendfindercommon.entity.Chat;
import com.friendfinder.friendfindercommon.entity.Message;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.repository.MessageRepository;
import com.friendfinder.friendfindercommon.service.ChatService;
import com.friendfinder.friendfindercommon.service.UserService;
import com.friendfinder.friendfindercommon.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    private MessageRepository messageRepository;
    private UserService userService;
    private ChatService chatService;
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        messageRepository = mock(MessageRepository.class);
        userService = mock(UserService.class);
        chatService = mock(ChatService.class);
        messageService = new MessageServiceImpl(messageRepository, userService, chatService);
    }

    @Test
    void testSaveWithValidData() {
        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setReceiverId(2);
        sendMessageDto.setChatId(1);
        sendMessageDto.setContent("Test message content");

        User currentUser = new User();
        currentUser.setId(1);

        User receiverUser = new User();
        receiverUser.setId(2);

        Chat chat = new Chat();
        chat.setId(1);

        when(userService.findUserById(sendMessageDto.getReceiverId())).thenReturn(Optional.of(receiverUser));
        when(chatService.findById(sendMessageDto.getChatId())).thenReturn(Optional.of(chat));

        boolean saved = messageService.save(sendMessageDto, currentUser);

        verify(messageRepository, times(1)).save(any(Message.class));

        assertTrue(saved);
    }

    @Test
    void testSaveWithInvalidReceiverId() {
        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setReceiverId(2);
        sendMessageDto.setChatId(1);
        sendMessageDto.setContent("Test message content");

        User currentUser = new User();
        currentUser.setId(1);

        when(userService.findUserById(sendMessageDto.getReceiverId())).thenReturn(Optional.empty());

        boolean saved = messageService.save(sendMessageDto, currentUser);

        verify(messageRepository, never()).save(any(Message.class));

        assertFalse(saved);
    }

    @Test
    void testSaveWithInvalidChatId() {
        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setReceiverId(2);
        sendMessageDto.setChatId(1);
        sendMessageDto.setContent("Test message content");

        User currentUser = new User();
        currentUser.setId(1);

        User receiverUser = new User();
        receiverUser.setId(2);

        when(userService.findUserById(sendMessageDto.getReceiverId())).thenReturn(Optional.of(receiverUser));
        when(chatService.findById(sendMessageDto.getChatId())).thenReturn(Optional.empty());

        boolean saved = messageService.save(sendMessageDto, currentUser);

        verify(messageRepository, never()).save(any(Message.class));

        assertFalse(saved);
    }

    @Test
    void testSaveWithNullInputs() {
        boolean saved = messageService.save(null, null);

        verify(messageRepository, never()).save(any(Message.class));

        assertFalse(saved);
    }
}