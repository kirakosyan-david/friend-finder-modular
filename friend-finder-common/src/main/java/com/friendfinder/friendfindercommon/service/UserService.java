package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.dto.userDto.UserLoginRequestDto;
import com.friendfinder.friendfindercommon.dto.userDto.UserRegisterRequestDto;
import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> userFindAll();

    List<Country> findAllCountries();

    List<User> userForAddFriend(CurrentUser currentUser);

    User userRegister(UserRegisterRequestDto dto);

    boolean userLogin(UserLoginRequestDto loginRequestDto);

    Optional<User> findByEmail(String email);

    void save(User user);

    Optional<User> findUserById(int id);

    List<User> findAllExceptCurrentUser(int currentUserId);

    Page<User> findAll(Pageable pageable);

    void deleteUserById(int id);

    boolean blockUserById(int id);

    boolean unblockUserById(int id);

    boolean changePassword(String oldPass, String newPass, String confPass, User user);
}
