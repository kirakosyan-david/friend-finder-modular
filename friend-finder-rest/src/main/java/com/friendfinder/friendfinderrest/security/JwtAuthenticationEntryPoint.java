package com.friendfinder.friendfinderrest.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

/**
 * The JwtAuthenticationEntryPoint class is an implementation of Spring Security's AuthenticationEntryPoint.
 * It is responsible for handling authentication failures and unauthorized access to protected resources.
 *
 * <p>When an unauthenticated user attempts to access a secured endpoint or resource, and the authentication fails,
 * this entry point is triggered. It responds with an HTTP 401 Unauthorized status code, indicating that the user
 * is not authorized to access the requested resource.
 *
 * <p>This class is registered as a component in the Spring context, making it available for use in the security
 * configuration. It is an essential component of the JWT-based authentication mechanism and ensures that users
 * receive appropriate error responses for unauthorized access attempts.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}