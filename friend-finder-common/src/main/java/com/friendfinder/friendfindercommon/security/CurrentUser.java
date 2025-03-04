package com.friendfinder.friendfindercommon.security;

import com.friendfinder.friendfindercommon.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Objects;

/**
 * <p>Custom implementation of the Spring Security User class, representing the currently authenticated user
 * along with associated details. This class extends the standard User class to provide additional functionality.
 * It holds an instance of the User class, which represents the user's core information, and inherits the fields
 * and methods from the Spring Security User class.</p>
 *
 * <p>The CurrentUser class is designed to be used in Spring Security configurations to represent the currently
 * authenticated user during the session. It allows easy access to user-specific details such as the user's
 * authorities (roles) and other information related to the authentication process.</p>
 *
 * <p>The constructor of this class takes a User object as a parameter, which contains the core information of the
 * associated user. The CurrentUser object can be accessed through the Authentication object provided by Spring
 * Security to retrieve user details and perform user-specific operations in service layers or controllers.</p>
 *
 * <p>Fields:</p>
 * <ul>- user: An instance of the User class representing the user whose details are associated with the currently
 *   authenticated user.</ul>
 *
 * <p>Methods:</p>
 * <ul>- getUser(): Returns the User object associated with the currently authenticated user. This method can be
 *   used to retrieve the core information of the user and perform user-specific operations.</ul>
 *
 * <p>Usage:
 * The CurrentUser class is commonly utilized in Spring Security configurations and authentication filters
 * to represent the currently authenticated user. It allows easy access to user details and permissions for
 * authorization and security purposes. Additionally, this class can be used in service layers or controllers
 * to access the details of the currently authenticated user and perform user-specific operations.</p>
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
