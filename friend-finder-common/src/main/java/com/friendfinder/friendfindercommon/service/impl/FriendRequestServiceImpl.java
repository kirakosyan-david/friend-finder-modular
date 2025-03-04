package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.repository.FriendRequestRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * FriendRequestServiceImpl is the implementation of the FriendRequestService interface, providing methods to interact
 * with the FriendRequestRepository and perform operations related to friend requests between users.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>friendRequestRepository: The FriendRequestRepository interface, allowing this service to interact with the
 *     database to perform CRUD operations on FriendRequest entities.</li>
 *     <li>userRepository: The UserRepository interface, providing access to user-related data and operations.</li>
 *     <li>mailService: An instance of the MailService used to send email notifications related to friend requests.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>save(friendRequest): Saves a new FriendRequest to the database. It checks if the friend request does not
 *     already exist (both directions: sender to receiver and receiver to sender) and sends an email notification to
 *     the receiver upon successful saving.</li>
 *     <li>findSenderByReceiverId(receiverId): Retrieves a list of users who sent friend requests to the specified
 *     receiverId. The method filters the list to include only requests with a status of "PENDING".</li>
 *     <li>findBySenderIdAndReceiverId(senderId, receiverId): Finds a FriendRequest based on the senderId and
 *     receiverId. If the request is found, it returns the FriendRequest object; otherwise, it returns null.</li>
 *     <li>delete(friendRequest): Deletes the specified FriendRequest from the database.</li>
 *     <li>userFriendsPageByUserId(userId, pageNumber): Retrieves a paginated list of user friends based on the
 *     userId and pageNumber. The method fetches the list of friends by calling findFriendsByUserId(userId) and then
 *     uses pagination to return a specific page of user friends.</li>
 *     <li>findFriendsByUserId(userId): Retrieves a list of user friends based on the userId. It searches through all
 *     FriendRequests and returns users who have an "ACCEPTED" status for friend requests related to the specified
 *     userId.</li>
 *     <li>changeStatus(friendRequest): Changes the status of a FriendRequest to "ACCEPTED". It also sends an email
 *     notification to the sender indicating that their friend request has been accepted.</li>
 *     <li>findFriendsByUserIdCount(id): Retrieves the count of user friends based on the userId.</li>
 *     <li>delete(sender, receiver): Deletes a friend request based on the sender and receiver users. It first checks
 *     if a FriendRequest exists in both directions (sender to receiver and receiver to sender) and then deletes the
 *     appropriate one.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * FriendRequestServiceImpl is used to manage friend request-related operations between users in the application. It is
 * typically utilized by the application's endpoints or controllers that handle user interactions for friend requests
 * and user friendship management. When a user sends a friend request or accepts a friend request, this service is
 * responsible for saving the requests to the database, changing request statuses, and sending email notifications to
 * the relevant users.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        FriendRequest friendRequests = null;
        if (findBySenderIdAndReceiverId(friendRequest.getSender().getId(), friendRequest.getReceiver().getId()) == null
                && findBySenderIdAndReceiverId(friendRequest.getReceiver().getId(), friendRequest.getSender().getId()) == null) {
            friendRequests = friendRequestRepository.save(friendRequest);
        }
        mailService.sendMail(friendRequest.getReceiver().getEmail(), "You have a new friend request", "Hi, "
                + friendRequest.getReceiver().getName() + ". You have an friend request from " +
                friendRequest.getSender().getName() + " " + friendRequest.getSender().getSurname());
        return friendRequests;
    }

    @Override
    public List<User> findSenderByReceiverId(int receiverId) {
        List<FriendRequest> frList = friendRequestRepository.findByReceiverId(receiverId);
        List<User> users = new ArrayList<>();
        for (FriendRequest friendRequest : frList) {
            if (friendRequest.getStatus() == FriendStatus.PENDING) {
                users.add(friendRequest.getSender());
            }
        }
        return users;
    }

    @Override
    public FriendRequest findBySenderIdAndReceiverId(int senderId, int receiverId) {
        Optional<FriendRequest> bySenderIdAndReceiverId = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        return bySenderIdAndReceiverId.orElse(null);
    }

    @Override
    public boolean delete(FriendRequest friendRequest) {
        Optional<FriendRequest> byId = friendRequestRepository.findById(friendRequest.getId());
        if (byId.isPresent()) {
            friendRequestRepository.delete(friendRequest);
            return true;
        }
        return false;
    }

    @Override
    public Page<User> userFriendsPageByUserId(int userId, int pageNumber) {
        List<User> friendsByUserId = findFriendsByUserId(userId);
        List<Integer> friendsId = new ArrayList<>();
        for (User user : friendsByUserId) {
            friendsId.add(user.getId());
        }
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNumber - 1, 12, sort);
        return userRepository.findUsersByIdIn(friendsId, pageable);
    }

    @Override
    public List<User> findFriendsByUserId(int userId) {
        List<FriendRequest> all = friendRequestRepository.findAll();
        List<User> result = new ArrayList<>();
        for (FriendRequest friendRequest : all) {
            if (friendRequest.getSender().getId() == userId && friendRequest.getStatus() == FriendStatus.ACCEPTED) {
                result.add(friendRequest.getReceiver());
            }
            if (friendRequest.getReceiver().getId() == userId && friendRequest.getStatus() == FriendStatus.ACCEPTED) {
                result.add(friendRequest.getSender());
            }
        }
        return result;
    }

    @Override
    public FriendRequest changeStatus(FriendRequest friendRequest) {
        friendRequest.setStatus(FriendStatus.ACCEPTED);
        mailService.sendMail(friendRequest.getSender().getEmail(), "Your friend request is accepted",
                "Hi, " + friendRequest.getSender().getName() +
                        ". " + friendRequest.getReceiver().getName() + " accepted your request.");
        friendRequestRepository.save(friendRequest);
        return friendRequest;
    }

    @Override
    public int findFriendsByUserIdCount(int id) {
        List<User> friendsByUserId = findFriendsByUserId(id);
        return friendsByUserId.size();
    }

    @Override
    public boolean delete(User sender, User receiver) {
        FriendRequest bySenderIdAndReceiverId = findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
        FriendRequest byReceiverIdAndSenderId = findBySenderIdAndReceiverId(receiver.getId(), sender.getId());
        if (bySenderIdAndReceiverId != null) {
            return delete(bySenderIdAndReceiverId);
        }
        if (byReceiverIdAndSenderId != null) {
            return delete(byReceiverIdAndSenderId);
        }
        return false;
    }

}
