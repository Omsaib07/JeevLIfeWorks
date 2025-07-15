package com.user.controller;

import com.user.dto.AuthRequest;
import com.user.model.User;
import com.user.service.UserService;
import com.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(409).body("Username already exists");
        }
        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            if (auth.isAuthenticated()) {
                String token = jwtUtil.generateToken(request.getUsername());
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(403).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Welcome " + auth.getName());
    }
}
