package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.repository.FriendRequestRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.service.impl.FriendRequestServiceImpl;
import com.friendfinder.friendfindercommon.service.impl.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.friendfinder.friendfinderrest.util.TestUtil.mockUserFirst;
import static com.friendfinder.friendfinderrest.util.TestUtil.mockUserSecond;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendRequestServiceTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private FriendRequestServiceImpl friendRequestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        friendRequestService = new FriendRequestServiceImpl(friendRequestRepository, userRepository, mailService);
    }

    @Test
    void testSaveFriendRequest_Success() {
        User sender = mockUserFirst();
        User receiver = mockUserSecond();
        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build();

        when(friendRequestRepository.save(friendRequest)).thenReturn(friendRequest);
        FriendRequest save = friendRequestService.save(friendRequest);
        verify(friendRequestRepository).save(friendRequest);
        assertEquals(friendRequest, save);
    }

    @Test
    void testFindSenderByReceiverId_Success() {
        int receiverId = 123;

        User sender1 = mockUserFirst();
        User receiver1 = mockUserSecond();

        User sender2 = mockUserFirst();
        User receiver2 = mockUserSecond();

        FriendRequest friendRequest1 = FriendRequest.builder()
                .status(FriendStatus.PENDING)
                .receiver(receiver1)
                .sender(sender1)
                .build();
        FriendRequest friendRequest2 = FriendRequest.builder()
                .status(FriendStatus.PENDING)
                .sender(sender2)
                .receiver(receiver2)
                .build();

        List<FriendRequest> friendRequestList = Arrays.asList(friendRequest1, friendRequest2);
        when(friendRequestRepository.findByReceiverId(receiverId)).thenReturn(friendRequestList);

        List<User> senderList = friendRequestService.findSenderByReceiverId(receiverId);

        assertNotNull(senderList);
        assertEquals(2, senderList.size());
    }

    @Test
    void testFindBySenderIdAndReceiverId_Found() {
        int senderId = 456;
        int receiverId = 789;

        User sender = mockUserFirst();
        User receiver = mockUserSecond();

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build();

        when(friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId)).thenReturn(Optional.of(friendRequest));

        FriendRequest foundFriendRequest = friendRequestService.findBySenderIdAndReceiverId(senderId, receiverId);

        assertNotNull(foundFriendRequest);
        assertEquals(friendRequest, foundFriendRequest);
    }

    @Test
    void testFindBySenderIdAndReceiverId_NotFound() {
        int senderId = 456;
        int receiverId = 789;

        when(friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId)).thenReturn(Optional.empty());

        FriendRequest foundFriendRequest = friendRequestService.findBySenderIdAndReceiverId(senderId, receiverId);

        assertNull(foundFriendRequest);
    }

    @Test
    void testDeleteFriendRequest_Success() {
        User sender = mockUserFirst();
        User receiver = mockUserSecond();

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build();

        when(friendRequestRepository.findById(friendRequest.getId())).thenReturn(Optional.of(friendRequest));

        boolean deleted = friendRequestService.delete(friendRequest);

        assertTrue(deleted);
        verify(friendRequestRepository, times(1)).delete(friendRequest);
    }

    @Test
    void testDeleteFriendRequest_NotFound() {
        User sender = mockUserFirst();
        User receiver = mockUserSecond();

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build();

        when(friendRequestRepository.findById(friendRequest.getId())).thenReturn(Optional.empty());

        boolean deleted = friendRequestService.delete(friendRequest);

        assertFalse(deleted);
        verify(friendRequestRepository, never()).delete(any(FriendRequest.class));
    }

    @Test
    void testUserFriendsPageByUserId_Success() {
        int userId = 100;
        List<User> friendsByUserId = Arrays.asList(
                mockUserFirst(),
                mockUserSecond(),
                mockUserFirst()
        );

        Page<User> users = new PageImpl<>(friendsByUserId);

        when(friendRequestRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findUsersByIdIn(anyList(), any())).thenReturn(users);

        Page<User> userPage = friendRequestService.userFriendsPageByUserId(userId, 1);

        assertNotNull(userPage);
        assertEquals(3, userPage.getTotalElements());
        assertEquals(friendsByUserId, userPage.getContent());
    }

    @Test
    void testFindFriendsByUserId_Success() {
        User sender1 = mockUserFirst();
        User receiver1 = mockUserSecond();

        User sender2 = mockUserFirst();
        User receiver2 = mockUserSecond();

        FriendRequest friendRequest1 = FriendRequest.builder()
                .status(FriendStatus.ACCEPTED)
                .receiver(receiver1)
                .sender(sender1)
                .build();
        FriendRequest friendRequest2 = FriendRequest.builder()
                .status(FriendStatus.ACCEPTED)
                .sender(sender2)
                .receiver(receiver2)
                .build();

        List<FriendRequest> allFriendRequests = Arrays.asList(
                friendRequest1,
                friendRequest2
        );

        when(friendRequestRepository.findAll()).thenReturn(allFriendRequests);

        List<User> friends = friendRequestService.findFriendsByUserId(sender1.getId());

        assertNotNull(friends);
        assertEquals(2, friends.size());
    }

    @Test
    void testChangeStatus_Success() {
        User sender = mockUserFirst();
        User receiver = mockUserSecond();

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build();


        FriendRequest updatedFriendRequest = friendRequestService.changeStatus(friendRequest);

        assertNotNull(updatedFriendRequest);
        assertEquals(FriendStatus.ACCEPTED, updatedFriendRequest.getStatus());
        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
    }

    @Test
    void testFindFriendsByUserIdCount_Success() {
        User user1 = mockUserFirst();
        User user2 = mockUserSecond();
        User user3 = mockUserFirst();
        FriendRequest friendRequest1 = FriendRequest.builder()
                .status(FriendStatus.ACCEPTED)
                .receiver(user1)
                .sender(user2)
                .build();
        FriendRequest friendRequest2 = FriendRequest.builder()
                .status(FriendStatus.ACCEPTED)
                .sender(user2)
                .receiver(user3)
                .build();
        FriendRequest friendRequest3 = FriendRequest.builder()
                .status(FriendStatus.ACCEPTED)
                .receiver(user3)
                .sender(user2)
                .build();

        List<FriendRequest> friendsByUserId = Arrays.asList(
                friendRequest1, friendRequest2, friendRequest3
        );

        when(friendRequestRepository.findAll()).thenReturn(friendsByUserId);

        int friendCount = friendRequestService.findFriendsByUserIdCount(user2.getId());

        assertEquals(3, friendCount);
    }
}
