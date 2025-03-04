package com.friendfinder.friendfinderrest.filter;


import com.friendfinder.friendfindercommon.security.CustomUserDetailsService;
import com.friendfinder.friendfinderrest.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The JWTAuthenticationTokenFilter class is a custom filter responsible for handling JWT-based authentication
 * for incoming requests. It intercepts the requests, extracts the JWT token from the "Authorization" header, and
 * validates it to authenticate users.
 *
 * <p>This filter extends OncePerRequestFilter, which ensures that the filter is only applied once per request,
 * avoiding multiple invocations. It is registered as a component in the Spring context, making it available for use
 * in the security configuration.
 *
 * <p>The filter performs the following tasks:
 * <ul>
 *   <li>Extracts the JWT token from the "Authorization" header of the incoming request.</li>
 *   <li>Validates the token and retrieves the username from the token using JwtTokenUtil.</li>
 *   <li>Loads the user details from the database using the CustomUserDetailsService based on the extracted username.</li>
 *   <li>
 *     If the token is valid and the user details are successfully loaded, it creates an instance of
 *     UsernamePasswordAuthenticationToken and sets it in the SecurityContextHolder to authenticate the user.
 *   </li>
 *   <li>
 *     The filter allows the request to proceed down the filter chain, ensuring that the request reaches the
 *     appropriate endpoint with the user authenticated in the SecurityContextHolder.
 *   </li>
 * </ul>
 *
 * <p>This filter plays a crucial role in the application's security by providing JWT-based authentication and
 * populating the SecurityContextHolder with authenticated user details. It ensures that authenticated users can
 * access secured endpoints and perform authorized actions.
 */
@Component
@RequiredArgsConstructor
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil tokenUtil;

    private final CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String requestHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String authToken = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);
            try {
                username = tokenUtil.getUsernameFromToken(authToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (tokenUtil.validateToken(authToken, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}