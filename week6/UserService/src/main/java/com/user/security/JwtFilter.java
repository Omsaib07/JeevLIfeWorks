package com.user.security;

//Import the UserService class (not directly used here, but may be used for extended logic)
import com.user.service.UserService;
//Import utility class responsible for JWT operations like extraction and validation
import com.user.util.JwtUtil;

//Import necessary classes for HTTP filtering and exception handling
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Lombok annotation to auto-generate constructor for final fields
import lombok.RequiredArgsConstructor;

//Spring Security classes for handling authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

//Marks this class as a Spring component for dependency injection
import org.springframework.stereotype.Component;
//Ensures the filter is executed only once per request
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Mark this class as a Spring-managed component
@Component
//Lombok will generate a constructor for final fields: jwtUtil and userDetailsService
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

 // Utility class to extract and validate JWT tokens
 private final JwtUtil jwtUtil;
 // Custom implementation of UserDetailsService to load user information from DB
 private final CustomUserDetailsService userDetailsService;

 // This method filters every HTTP request and applies JWT-based authentication
 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {

     // Extract the Authorization header from the HTTP request
     final String authHeader = request.getHeader("Authorization");
     String username = null;
     String jwt = null;

     // If the header exists and starts with "Bearer ", extract the JWT token and username
     if (authHeader != null && authHeader.startsWith("Bearer ")) {
         jwt = authHeader.substring(7); // Remove "Bearer " prefix
         username = jwtUtil.extractUsername(jwt); // Extract username from token
     }

     // If username is extracted and user is not already authenticated
     if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
         // Load user details using the custom UserDetailsService
         UserDetails userDetails = userDetailsService.loadUserByUsername(username);

         // Validate the token using the username from the userDetails
         if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
             // Create an authentication token containing user details and roles
             UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                     userDetails, null, userDetails.getAuthorities());

             // Attach request details like IP address and session ID
             authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

             // Set the authentication object in Spring Security's context
             SecurityContextHolder.getContext().setAuthentication(authToken);
         }
     }

     // Pass the request to the next filter in the chain
     filterChain.doFilter(request, response);
 }
}
