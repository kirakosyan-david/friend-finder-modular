package com.friendfinder.friendfinderrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"com.friendfinder.friendfinderrest", "com.friendfinder.friendfindercommon"})
@EntityScan(basePackages = "com.friendfinder.friendfindercommon.entity")
@EnableJpaRepositories(basePackages = "com.friendfinder.friendfindercommon.repository")
public class FriendFinderRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendFinderRestApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
