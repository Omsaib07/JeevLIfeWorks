package com.ems.security;
//Import core JWT classes like Claims, JwtParser, etc.
import io.jsonwebtoken.*;
//Import utility for generating secure signing keys
import io.jsonwebtoken.security.Keys;

//Import Spring's UserDetails interface for accessing user info
import org.springframework.security.core.userdetails.UserDetails;
//Mark this class as a Spring-managed service component
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

//Marks this class as a service component to be auto-detected by Spring
@Service
public class JwtService {

 // Secret key used for signing and verifying JWT tokens (must be Base64-encoded and at least 256 bits for HS256)
 private static final String SECRET_KEY = "VPUtzN8zjahLetTxS2hjhcKp1hBDfZtnTA8v41GMvyDRRKcSO+YjIqmaVNDxkCDUKD7aN4a+hJctGaUx/no20g==";

 // Converts the secret string into a Key object used for signing and verifying JWTs
 private Key getSigningKey() {
     return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
 }

 // Parses the JWT token and returns all claims (payload data)
 public Claims extractAllClaims(String token) {
     return Jwts.parserBuilder()                 // Create a new JwtParserBuilder instance
             .setSigningKey(getSigningKey())     // Set the signing key used for verification
             .build()                            // Build the parser
             .parseClaimsJws(token)              // Parse the token and validate signature
             .getBody();                         // Return the claims section of the token
 }

 // Validates if the token is valid (not expired and correctly signed)
 public boolean isTokenValid(String token) {
     try {
         // Extract claims to check expiration
         Claims claims = extractAllClaims(token);
         // Check if the token has expired
         return !claims.getExpiration().before(new Date());
     } catch (Exception e) {
         // Return false if token parsing or validation fails
         return false;
     }
 }
}
