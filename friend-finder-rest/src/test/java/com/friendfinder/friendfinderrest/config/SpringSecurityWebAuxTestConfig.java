package com.friendfinder.friendfinderrest.config;

import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.Date;

@TestConfiguration
public class SpringSecurityWebAuxTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        Country country = new Country(1, "Afghanistan");
        User user = new User(1, "user", "user", "user@friendfinder.com", "user",
                new Date(1990, 5, 15), UserGender.MALE, "New York", country,
                null, null, "Some personal info",
                true, null, UserRole.ADMIN);

        CurrentUser currentUser = new CurrentUser(user);

        return new InMemoryUserDetailsManager(Arrays.asList(
                currentUser
        ));
    }
}