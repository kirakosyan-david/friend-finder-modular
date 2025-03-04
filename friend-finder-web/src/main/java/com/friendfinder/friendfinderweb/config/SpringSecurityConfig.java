package com.friendfinder.friendfinderweb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/verify").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/admin-css/**").permitAll()
                        .requestMatchers("/admin-js/**").permitAll()
                        .requestMatchers("/fonts/**").permitAll()
                        .requestMatchers("/icon/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/posts/images/page/**").permitAll()
                        .anyRequest().hasAnyAuthority("ADMIN", "USER")
                )
                .formLogin(form -> form
                        .loginPage("/login-register")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/posts")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );


        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
}
