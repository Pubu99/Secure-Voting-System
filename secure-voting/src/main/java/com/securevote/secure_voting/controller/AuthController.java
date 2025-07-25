package com.securevote.secure_voting.controller;

import com.securevote.secure_voting.model.User;
import com.securevote.secure_voting.repository.UserRepository;
import com.securevote.secure_voting.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser != null && dbUser.getPassword().equals(user.getPassword())) {
            String token = jwtUtil.generateToken(dbUser.getUsername());
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
