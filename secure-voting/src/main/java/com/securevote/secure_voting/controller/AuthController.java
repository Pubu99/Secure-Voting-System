package com.securevote.secure_voting.controller;

import com.securevote.secure_voting.model.User;
import com.securevote.secure_voting.repository.UserRepository;
import com.securevote.secure_voting.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public String login(@RequestBody User loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && passwordEncoder.matches(loginRequest.getPasswordHash(), user.getPasswordHash())) {
            return jwtUtil.generateToken(user.getUsername());
        } else {
            return "Invalid credentials";
        }
    }
}
