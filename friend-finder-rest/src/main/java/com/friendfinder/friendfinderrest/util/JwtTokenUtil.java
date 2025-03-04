package com.friendfinder.friendfinderrest.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The JwtTokenUtil class is a utility component responsible for handling JSON Web Tokens (JWT) related operations.
 * It provides methods to generate, parse, and validate JWT tokens used for user authentication and authorization.
 *
 * <p>This class is annotated with @Component, making it a Spring Bean and enabling its automatic detection and
 * registration within the Spring application context.
 *
 * <p>The JwtTokenUtil uses the io.jsonwebtoken library to perform JWT operations. It requires a secret key and an
 * expiration time for token generation and validation, which are typically provided through configuration properties.
 *
 * <p>The JwtTokenUtil has the following functionalities:
 * - Generating a JWT token for a given email address (subject) and optional claims.
 * - Parsing a JWT token and extracting the subject and other claims from it.
 * - Checking if a token has expired.
 * - Refreshing a token by extending its expiration date.
 * - Validating a token by checking its subject (email) and expiration date.
 *
 * <p>The secret key used for token signing is expected to be Base64-encoded, and it is converted into a cryptographic
 * Key using the io.jsonwebtoken.security.Keys class.
 *
 * <p>This class is a critical component of the JWT-based authentication mechanism, ensuring secure token generation,
 * validation, and parsing.
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, email);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    public String refreshToken(String token) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token, String email) {

        final String username = getUsernameFromToken(token);
        return (
                username.equals(email)
                        && !isTokenExpired(token));
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}