package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.dto.userDto.UserLoginRequestDto;
import com.friendfinder.friendfindercommon.dto.userDto.UserRegisterRequestDto;
import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.exception.custom.UserLoginException;
import com.friendfinder.friendfindercommon.mapper.UserRegisterMapper;
import com.friendfinder.friendfindercommon.repository.CountryRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * UserServiceImpl is a service class that provides methods for managing user-related functionalities in the application.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>passwordEncoder: The PasswordEncoder interface used to encode passwords for secure storage and authentication.</li>
 *     <li>friendRequestService: The FriendRequestService interface used to manage friend requests between users.</li>
 *     <li>userRegisterMapper: The UserRegisterMapper interface used to map user registration data between DTO and entity.</li>
 *     <li>countryRepository: The CountryRepository interface used to access and retrieve country data from the database.</li>
 *     <li>userRepository: The UserRepository interface used to access and retrieve user data from the database.</li>
 *     <li>mailService: The MailService interface used to send email notifications to users.</li>
 *     <li>siteUrl: The URL of the site where the application is hosted, used for email verification links.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>userForAddFriend(currentUser): Retrieves a list of users who can be added as friends by the current user.
 *     The method filters out users who are already friends or have pending friend requests with the current user.</li>
 *     <li>findAllCountries(): Retrieves a list of all countries available in the application.</li>
 *     <li>userRegister(dto): Registers a new user in the application based on the provided user registration data in the UserRegisterRequestDto.
 *     The method checks if the user with the given email already exists. If not, it encodes the password, generates a verification token,
 *     sends an email verification link, and saves the user to the database with the initial role and enabled status set to false.</li>
 *     <li>updateUserPasswordById(password, id): Updates the user's password in the database based on the provided user ID.
 *     The method uses the UserRepository to update the user's password in the database.</li>
 *     <li>findByEmail(email): Retrieves a user from the database based on the provided email.</li>
 *     <li>save(user): Saves a user entity in the database using the UserRepository.</li>
 *     <li>findUserById(id): Retrieves a user from the database based on the provided user ID.</li>
 *     <li>findAllExceptCurrentUser(currentUserId): Retrieves a list of all users in the application except the current user.
 *     The method filters out the current user from the list of users.</li>
 *     <li>findAll(pageable): Retrieves a paginated list of all users in the application from the database using the UserRepository.</li>
 *     <li>userFindAll(): Retrieves a list of all users in the application from the database using the UserRepository.</li>
 *     <li>deleteUserById(id): Deletes a user from the database based on the provided user ID.</li>
 *     <li>blockUserById(id): Blocks a user in the application based on the provided user ID.
 *     The method updates the user's role to "BLOCKED" and saves the changes to the database.</li>
 *     <li>unblockUserById(id): Unblocks a user in the application based on the provided user ID.
 *     The method updates the user's role to "USER" and saves the changes to the database.</li>
 *     <li>changePassword(oldPass, newPass, confPass, user): Changes the user's password in the application.
 *     The method verifies the old password, validates the new password, and updates the user's password in the database if the conditions are met.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * UserServiceImpl provides various functionalities for managing users in the application. It handles user registration, updating user information,
 * managing user passwords, and handling user interactions such as adding friends and blocking/unblocking users. The service communicates with
 * the UserRepository, CountryRepository, and FriendRequestService to access and modify user data in the database. By integrating these
 * functionalities, the service enables users to perform various actions and ensures a smooth user experience within the social media platform.
 * The service also utilizes the PasswordEncoder for secure password storage and verification, enhancing the overall security of the application.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final FriendRequestService friendRequestService;
    private final UserRegisterMapper userRegisterMapper;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;

    private final MailService mailService;
    @Value("${site.url}")
    String siteUrl;

    @Override
    public List<User> userForAddFriend(CurrentUser currentUser) {
        List<User> users = userFindAll();
        List<User> userForAddFriend = new ArrayList<>();
        for (User user : users) {
            if (friendRequestService.findBySenderIdAndReceiverId(user.getId(), currentUser.getUser().getId()) == null &&
                    user.getId() != currentUser.getUser().getId() && friendRequestService.findBySenderIdAndReceiverId(currentUser.getUser().getId(),
                    user.getId()) == null) {
                userForAddFriend.add(user);
            }
        }
        return userForAddFriend;
    }

    @Override
    public List<Country> findAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public User userRegister(UserRegisterRequestDto dto) {
        User user = userRegisterMapper.map(dto);

        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isEmpty()) {
            String password = user.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            user.setEnabled(false);
            user.setRole(UserRole.USER);
            UUID uuid = UUID.randomUUID();
            user.setToken(uuid.toString());
            mailService.sendMail(user.getEmail(), "Verify Email",
                    "Hi " + user.getName() + "!\nPlease verify your email by clicking on this URL:\n " +
                            siteUrl + "/verify?email=" + user.getEmail() + "&token=" + uuid
            );
            return userRepository.save(user);
        }

        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean userLogin(UserLoginRequestDto loginRequestDto) {
        Optional<User> byEmail = findByEmail(loginRequestDto.getEmail());
        if (byEmail.isEmpty()) {
            log.error("User email is incorrect", new UsernameNotFoundException("username: " + loginRequestDto.getEmail()));
            return false;
        }

        User user = byEmail.get();
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            log.error("Wrong password", new UserLoginException("wrong user password"));
            return false;
        }

        return true;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAllExceptCurrentUser(int currentUserId) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getId() != currentUserId)
                .collect(Collectors.toList());
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> userFindAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean blockUserById(int id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) return false;

        User user = userById.get();
        user.setRole(UserRole.BLOCKED);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean unblockUserById(int id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) return false;

        User user = userById.get();
        user.setRole(UserRole.USER);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean changePassword(String oldPass, String newPass, String confPass, User user) {
        if (passwordEncoder.matches(oldPass, user.getPassword()) && newPass.equals(confPass)) {
            String encodedPass = passwordEncoder.encode(newPass);
            user.setPassword(encodedPass);
            userRepository.updateUserPasswordById(encodedPass, user.getId());
            return true;
        }
        return false;
    }
}
