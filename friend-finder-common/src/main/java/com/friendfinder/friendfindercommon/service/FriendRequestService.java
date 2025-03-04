package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FriendRequestService {

    Page<User> userFriendsPageByUserId(int userId, int pageNumber);

    FriendRequest save(FriendRequest friendRequest);

    List<User> findSenderByReceiverId(int receiverId);

    FriendRequest findBySenderIdAndReceiverId(int senderId, int receiverId);

    boolean delete(FriendRequest friendRequest);

    boolean delete(User sender, User receiver);

    List<User> findFriendsByUserId(int userId);

    FriendRequest changeStatus(FriendRequest friendRequest);

    int findFriendsByUserIdCount(int id);

}
