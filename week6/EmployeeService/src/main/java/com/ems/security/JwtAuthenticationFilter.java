package com.ems.security;


//Importing necessary classes for IO exceptions
import java.io.IOException;
//Used for assigning default authorities if no roles are found
import java.util.Collections;
//Importing List interface and stream operations
import java.util.List;
import java.util.stream.Collectors;

//Spring Security class for creating authentication tokens
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//Used to represent granted roles/authorities
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//Holds security context for the current thread
import org.springframework.security.core.context.SecurityContextHolder;
//Builds details like IP address and session ID
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//Marks this class as a Spring-managed component (bean)
import org.springframework.stereotype.Component;
//Ensures filter runs once per request
import org.springframework.web.filter.OncePerRequestFilter;

//Used for parsing JWT claims (payload)
import io.jsonwebtoken.Claims;
//Required for servlet-based filter chaining
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
//Represents HTTP request and response objects
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Lombok annotation to generate constructor for final fields
import lombok.RequiredArgsConstructor;

//Marks this class as a Spring component so it can be injected where needed
@Component
//Lombok will generate a constructor for any final fields (like jwtService)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

 // Service used to validate and extract information from JWT
 private final JwtService jwtService;

 // This method intercepts every HTTP request once and applies security logic
 @Override
 protected void doFilterInternal(
         HttpServletRequest request,
         HttpServletResponse response,
         FilterChain filterChain
 ) throws ServletException, IOException {

     // Retrieve the Authorization header from the incoming request
     final String authHeader = request.getHeader("Authorization");

     // If no Authorization header or it doesn't start with "Bearer ", skip filtering
     if (authHeader == null || !authHeader.startsWith("Bearer ")) {
         filterChain.doFilter(request, response);
         return;
     }

     // Extract the JWT token by removing the "Bearer " prefix
     final String jwtToken = authHeader.substring(7);

     // Validate the token; if invalid, skip authentication
     if (!jwtService.isTokenValid(jwtToken)) {
         filterChain.doFilter(request, response);
         return;
     }

     // Extract all claims (payload) from the token
     Claims claims = jwtService.extractAllClaims(jwtToken);
     // Get the username (subject) from the token claims
     String username = claims.getSubject();

     // Extract roles from the claims (if present); otherwise, assign default ROLE_USER
     List<String> roles = claims.get("roles", List.class);
     List<SimpleGrantedAuthority> authorities = roles == null
             ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
             : roles.stream()
                 .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                 .collect(Collectors.toList());

     // Create an authentication token with username and roles (no password required)
     UsernamePasswordAuthenticationToken authToken =
             new UsernamePasswordAuthenticationToken(username, null, authorities);

     // Set additional details (like remote address, session ID)
     authToken.setDetails(
             new WebAuthenticationDetailsSource().buildDetails(request)
     );

     // Set authentication in the Spring Security context
     SecurityContextHolder.getContext().setAuthentication(authToken);

     // Continue with the filter chain (pass control to the next filter)
     filterChain.doFilter(request, response);
 }
}
