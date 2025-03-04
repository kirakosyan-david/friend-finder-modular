package com.friendfinder.friendfindercommon.security;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>CustomUserDetailsService is an implementation of the Spring Security UserDetailsService interface,
 * which is responsible for loading user-specific data during the authentication process. This service
 * is designed to retrieve user details from the UserRepository based on the provided username (email),
 * and then create a CurrentUser object to represent the currently authenticated user with associated
 * user details.</p>
 *
 * <p>This service class is typically used in Spring Security configurations to handle user authentication.
 * When a user attempts to log in, the loadUserByUsername method is called, and it retrieves the user's
 * information from the UserRepository. If the user is found, a CurrentUser object is created using the
 * retrieved User information, and this CurrentUser object is returned as a UserDetails object to Spring
 * Security. The CurrentUser class extends the standard Spring Security User class and provides additional
 * functionality to access user-specific details.</p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>userRepository: The UserRepository interface, which allows this service to interact with the database
 *     to find user details based on the provided username (email).</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>loadUserByUsername(username): This method implements the UserDetailsService interface and is called
 *     during the authentication process. It takes the username (email) as a parameter, queries the UserRepository
 *     to find the user, and returns a CurrentUser object representing the authenticated user. If the user does
 *     not exist, a UsernameNotFoundException is thrown.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>The CustomUserDetailsService class is typically used as a part of the Spring Security configuration to
 * handle user authentication. It is registered as the UserDetailsService to provide user-specific details
 * during the login process. By using this service, Spring Security can retrieve user information from the
 * database and create a CurrentUser object, which holds the user's core details, roles, and permissions. The
 * CurrentUser object is then used for authentication and authorization purposes within the application.</p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(username);
        if (byEmail.isEmpty()) {
            throw new UsernameNotFoundException("user does not exists");
        }

        return new CurrentUser(byEmail.get());
    }
}
