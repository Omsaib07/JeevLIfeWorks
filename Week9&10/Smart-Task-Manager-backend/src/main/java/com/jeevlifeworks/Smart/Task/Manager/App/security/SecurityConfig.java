package com.jeevlifeworks.Smart.Task.Manager.App.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jeevlifeworks.Smart.Task.Manager.App.service.UserDetailsServiceImpl;

/**
 * Configuration class for Spring Security setup.
 * It sets up authentication, authorization, and security filter chain using JWT.
 */
@Configuration
@EnableWebSecurity // Enables Spring Security’s web security support
@EnableMethodSecurity(prePostEnabled = true) // Enables method-level security with @PreAuthorize
@EnableScheduling // Enables scheduling for tasks
public class SecurityConfig {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	/**
	 * Bean for password encoding using BCrypt algorithm.
	 * Used for hashing user passwords.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Authentication provider that uses UserDetailsService and PasswordEncoder.
	 * Connects Spring Security with our custom user fetching logic.
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService); // Custom logic to fetch user
		authProvider.setPasswordEncoder(passwordEncoder()); // Use BCrypt for password matching
		return authProvider;
	}

	/**
	 * Bean to expose AuthenticationManager for authenticating credentials.
	 * Required for login authentication.
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * Configures the HTTP security filter chain:
	 * - Disables CSRF (since we use JWT, not sessions)
	 * - Sets session policy to STATELESS
	 * - Allows unauthenticated access to auth and Swagger endpoints
	 * - Secures all other endpoints
	 * - Adds JWT filter before UsernamePasswordAuthenticationFilter
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers("/api/auth/**").permitAll() // Public endpoints for auth
				.anyRequest().authenticated() // All other endpoints require authentication
				);

		http.authenticationProvider(authenticationProvider()); // Set the authentication provider
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

		// Build the security chain
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(List.of("http://localhost:4200"));
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(List.of("*"));
	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
}
