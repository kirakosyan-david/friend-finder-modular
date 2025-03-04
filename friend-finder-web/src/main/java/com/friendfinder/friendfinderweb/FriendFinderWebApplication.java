package com.friendfinder.friendfinderweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"com.friendfinder.friendfinderweb", "com.friendfinder.friendfindercommon"})
@EntityScan(basePackages = "com.friendfinder.friendfindercommon.entity")
@EnableJpaRepositories(basePackages = "com.friendfinder.friendfindercommon.repository")
public class FriendFinderWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendFinderWebApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
