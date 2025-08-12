package com.jeevlifeworks.Smart.Task.Manager.App.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jeevlifeworks.Smart.Task.Manager.App.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// OncePerRequestFilter Ensures the filter executes once per HTTP request
@Service
@Slf4j // Enables logging using log.debug, log.error, etc.
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired 
	private JwtTokenUtil jwtUtil;
	
    @Autowired 
    private UserDetailsServiceImpl userDetailsService;

    /**
     * This method is called for every request. 
     * It handles token extraction, validation, and setting authentication.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

    	try {
    		// Extracts the token from the request header using a helper method.
            String jwt = parseJwt(request);
            if (jwt != null) {
            	// Gets the email (username) encoded in the JWT
                String userEmail = jwtUtil.extractUsername(jwt);
                
                /**
                 *  Ensures user isn't already authenticated.
                 *  Loads user details from DB.
                 */
                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                    //If the token is valid, a new authentication token is created with user roles/authorities.
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                        // Sets the authentication in the SecurityContextHolder, making the user officially "logged in" for this request.
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("Authenticated user: "+ userEmail);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: "+ e.getMessage());
        }
    	// continue the chain
        filterChain.doFilter(request, response); 
    }
    
    /**
     * Helper method 
     * Safely parses the JWT from the Authorization header.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
