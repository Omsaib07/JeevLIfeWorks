package com.jeevlifeworks.Smart.Task.Manager.App.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenUtil {

	@Value("${app.jwt.secret}")
	private String secret;

	@Value("${app.jwt.expiration}")
	private long expiration;

	// Generate JWT token using user details
	public String generateToken(UserDetails userDetails) {
		// Create a claims map
		Map<String, Object> claims = new HashMap<>();
		
		// Extract the user's roles and add them to the claims map
				claims.put("roles", userDetails.getAuthorities().stream()
						.map(authority -> authority.getAuthority())
						.collect(Collectors.toList()));
				
		log.info("Generating JWT for user: "+ userDetails.getUsername() + " with roles: " + claims.get("roles"));
		return createToken(claims, userDetails.getUsername());
	}

	// Build the token with subject and expiration
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims) // Pass the populated claims map to the builder
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	// Validates token: checks username and expiration
	public Boolean validateToken(String token, UserDetails userDetails) {
		try {
			final String username = extractUsername(token);
			boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
			if (!isValid) {
				log.warn("Invalid JWT: Token username does not match user details or is expired.");
			}
			return isValid;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: "+ e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: "+ e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: "+ e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: "+ e.getMessage());
		}
		return false;
	}

	// Extract username from token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Extract specific claim using resolver function
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Extract all claims from token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	// Check if token is expired
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Extract expiration time from token
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// Generate signing key from base64-encoded secret
	private Key getSigningKey() {
		byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
