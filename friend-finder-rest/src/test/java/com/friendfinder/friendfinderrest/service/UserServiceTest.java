package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.mapper.UserRegisterMapper;
import com.friendfinder.friendfindercommon.repository.CountryRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.impl.MailService;
import com.friendfinder.friendfindercommon.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.friendfinder.friendfinderrest.util.TestUtil.mockCurrentUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private FriendRequestService friendRequestService;

    @Mock
    private UserRegisterMapper userRegisterMapper;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailService mailService;

    @Mock
    private CurrentUser currentUser;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(passwordEncoder, friendRequestService, userRegisterMapper, countryRepository, userRepository, mailService);
    }

    @Test
    void testBlockUserById() {
        int userId = 1;

        User user = new User();
        user.setId(userId);
        user.setRole(UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        boolean result = userService.blockUserById(userId);

        verify(userRepository).save(user);

        assertEquals(UserRole.BLOCKED, user.getRole());
        assertEquals(true, result);
    }

    @Test
    void testUnblockUserById() {
        int userId = 1;

        User user = new User();
        user.setId(userId);
        user.setRole(UserRole.BLOCKED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        boolean result = userService.unblockUserById(userId);

        verify(userRepository).save(user);

        assertEquals(UserRole.USER, user.getRole());
        assertEquals(true, result);
    }

    @Test
    void testUserForAddFriend() {
        int currentUserUserId = 1;

        User currentUserUser = mockCurrentUser().getUser();
        currentUserUser.setId(currentUserUserId);
        CurrentUser currentUser = new CurrentUser(currentUserUser);

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        for (User user : users) {
            when(friendRequestService.findBySenderIdAndReceiverId(user.getId(), currentUserUserId)).thenReturn(null);
        }

        List<User> result = userService.userForAddFriend(currentUser);

        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(user -> user.getId() != currentUserUserId));
    }

    @Test
    void testFindAllCountries() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1, "Country 1"));
        countries.add(new Country(2, "Country 2"));

        when(countryRepository.findAll()).thenReturn(countries);

        List<Country> result = userService.findAllCountries();

        assertEquals(2, result.size());
        assertEquals(countries, result);
    }

    @Test
    void testFindUserById() {
        int userId = 1;

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testFindUserById_UserNotFound() {
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllExceptCurrentUser() {
        int currentUserId = 1;

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllExceptCurrentUser(currentUserId);

        assertEquals(2, result.size());
        assertTrue(result.stream().noneMatch(user -> user.getId() == currentUserId));
    }

    @Test
    void testFindAll() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(users));

        Page<User> result = userService.findAll(pageable);

        assertEquals(3, result.getTotalElements());
        assertTrue(result.stream().allMatch(users::contains));
    }

    @Test
    void testUserFindAll() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        when(userService.userFindAll()).thenReturn(users);

        List<User> result = userService.userFindAll();

        assertEquals(3, result.size());
        assertEquals(users, result);
    }

    @Test
    void testDeleteUserById() {
        int userId = 1;

        userService.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
    }

}