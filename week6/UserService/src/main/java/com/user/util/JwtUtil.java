package com.user.util;

//JWT library core classes
import io.jsonwebtoken.*;
//Utility for secure key generation
import io.jsonwebtoken.security.Keys;

//Spring annotation to inject values from application.properties
import org.springframework.beans.factory.annotation.Value;
//Marks this as a Spring-managed component (bean)
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

//Marks this class as a Spring component so it can be injected where needed
@Component
public class JwtUtil {

 // Injects the secret key value from application.properties (e.g., jwt.secret=your-secret-key)
 @Value("${jwt.secret}")
 private String secret;

 // Returns the signing key generated from the secret string using HMAC SHA algorithm
 private Key getSigningKey() {
     return Keys.hmacShaKeyFor(secret.getBytes());
 }

 // Generates a JWT token for the given username
 public String generateToken(String username) {
     return Jwts.builder()
             .setSubject(username) // Set the username as the token's subject (main identity)
             .setIssuedAt(new Date()) // Set the current time as issue time
             .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Set token to expire in 10 hours
             .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign the token with HMAC-SHA256
             .compact(); // Build and return the JWT token string
 }

 // Validates the token by checking the username and expiration
 public Boolean validateToken(String token, String username) {
     // Extract the username from token and compare, also check if it's not expired
     return extractUsername(token).equals(username) && !isTokenExpired(token);
 }

 // Extracts the username (subject) from the token
 public String extractUsername(String token) {
     return extractClaim(token, Claims::getSubject); // Uses a claim resolver to get the subject field
 }

 // Extracts the expiration date from the token
 public Date extractExpiration(String token) {
     return extractClaim(token, Claims::getExpiration); // Uses a claim resolver to get the expiration date
 }

 // Generic method to extract any claim from the token using a lambda function
 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
     final Claims claims = Jwts.parserBuilder() // Create a JWT parser
             .setSigningKey(getSigningKey())     // Set the signing key for verifying the token
             .build()
             .parseClaimsJws(token)              // Parse and verify the JWT token
             .getBody();                         // Get the claims (payload) part
     return claimsResolver.apply(claims);        // Apply the resolver function to extract specific claim
 }

 // Checks whether the token has expired
 private Boolean isTokenExpired(String token) {
     return extractExpiration(token).before(new Date()); // If expiration is before now, it's expired
 }
}
