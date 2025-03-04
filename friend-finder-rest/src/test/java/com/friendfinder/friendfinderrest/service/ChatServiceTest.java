package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.entity.Chat;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.mapper.UserRegisterMapper;
import com.friendfinder.friendfindercommon.repository.ChatRepository;
import com.friendfinder.friendfindercommon.repository.CountryRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.impl.ChatServiceImpl;
import com.friendfinder.friendfindercommon.service.impl.MailService;
import com.friendfinder.friendfindercommon.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.friendfinder.friendfinderrest.util.TestUtil.mockUserFirst;
import static com.friendfinder.friendfinderrest.util.TestUtil.mockUserSecond;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    ChatRepository chatRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    FriendRequestService friendRequestService;
    @Mock
    UserRegisterMapper userRegisterMapper;
    @Mock
    CountryRepository countryRepository;
    @Mock
    MailSender mailSender;

    @InjectMocks
    private ChatServiceImpl chatService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MailService mailService = new MailService(mailSender);
        chatService = new ChatServiceImpl(chatRepository, userService);
        userService = new UserServiceImpl(passwordEncoder, friendRequestService, userRegisterMapper, countryRepository, userRepository, mailService);
    }

    @Test
    void testFindAllByCurrentUserId() {
        int currentUserId = 1;

        List<Chat> expectedChats = new ArrayList<>();
        expectedChats.add(new Chat());
        expectedChats.add(new Chat());
        expectedChats.add(new Chat());

        when(chatService.findAllByCurrentUserId(currentUserId)).thenReturn(expectedChats);
        List<Chat> resultChats = chatService.findAllByCurrentUserId(currentUserId);

        assertEquals(expectedChats, resultChats);
    }

    @Test
    void testFindAllByAnotherUserId() {
        int anotherUserId = 3;
        List<Chat> expectedChats = new ArrayList<>();
        expectedChats.add(new Chat());
        expectedChats.add(new Chat());
        expectedChats.add(new Chat());

        when(chatService.findAllByAnotherUserId(anotherUserId)).thenReturn(expectedChats);
        List<Chat> resultChats = chatService.findAllByAnotherUserId(anotherUserId);

        assertEquals(expectedChats, resultChats);
    }

    @Test
    void testFindByCurrentUserIdAndAnotherUserId() {
        int firstId = 12;
        int secondId = 3;

        Chat expectedChat = new Chat();
        when(chatRepository.findByCurrentUserIdAndAnotherUserId(firstId, secondId)).thenReturn(Optional.of(expectedChat));
        Optional<Chat> resultChat = chatService.findByCurrentUserIdAndAnotherUserId(firstId, secondId);

        assertTrue(resultChat.isPresent());
        assertEquals(expectedChat, resultChat.get());
    }

    @Test
    void testFindById() {
        int chatId = 1;
        Chat expectedChat = new Chat();

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(expectedChat));
        Optional<Chat> resultChat = chatRepository.findById(chatId);

        assertTrue(resultChat.isPresent());
        assertEquals(expectedChat, resultChat.get());
    }

    @Test
    void testSave() {
        User userFirst = mockUserFirst();
        User userSecond = mockUserSecond();
        Chat chat = new Chat();
        chat.setAnotherUser(userFirst);
        chat.setCurrentUser(userSecond);
        chatService.save(chat);

        verify(chatRepository, times(1)).save(chat);
    }

    @Test
    void testCreate() {
        int userId = 1;
        User user = new User();
        user.setId(2);

        User currentUser = new User();
        currentUser.setId(1);

        when(userService.findUserById(userId)).thenReturn(Optional.of(currentUser));
        when(chatRepository.findByCurrentUserIdAndAnotherUserId(user.getId(), userId)).thenReturn(Optional.empty());
        when(chatRepository.findByCurrentUserIdAndAnotherUserId(userId, user.getId())).thenReturn(Optional.empty());

        boolean result = chatService.create(userId, user);

        assertTrue(result);
        verify(chatRepository, times(1)).save(any());
    }

    @Test
    void testCreateSameUserIds() {
        int userId = 1;
        User user = new User();
        user.setId(1);

        boolean result = chatService.create(userId, user);

        assertFalse(result);
        verify(chatRepository, never()).save(any());
    }

    @Test
    void testCreateUserNotFound() {
        int userId = 1;
        User user = new User();
        user.setId(2);

        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        boolean result = chatService.create(userId, user);

        assertFalse(result);
        verify(chatRepository, never()).save(any());
    }

    @Test
    void testCreateChatAlreadyExists() {
        int userId = 1;
        User user = new User();
        user.setId(2);

        User currentUser = new User();
        currentUser.setId(1);

        when(userService.findUserById(userId)).thenReturn(Optional.of(currentUser));
        when(chatRepository.findByCurrentUserIdAndAnotherUserId(user.getId(), userId)).thenReturn(Optional.of(new Chat()));

        boolean result = chatService.create(userId, user);

        assertFalse(result);
        verify(chatRepository, never()).save(any());
    }
}