package com.securevote.secure_voting.controller;

import com.securevote.secure_voting.dto.AuthRequest;
import com.securevote.secure_voting.model.User;
import com.securevote.secure_voting.repository.UserRepository;
import com.securevote.secure_voting.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        user.setJwtToken(token);
        userRepository.save(user);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
        try {
            Optional<User> existingUser = userRepository.findByUsername(authRequest.getUsername());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }

            User user = new User();
            user.setUsername(authRequest.getUsername());
            user.setPasswordHash(passwordEncoder.encode(authRequest.getPassword()));
            user.setRole("USER");
            user.setHasVoted(false);
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            log.error("Registration failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed due to server error");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body("Logged out successfully");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authHeader.replace("Bearer ", "").trim();
            String username = jwtUtil.extractUsername(token);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = userOptional.get();
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to retrieve user details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving user details");
        }
    }
}