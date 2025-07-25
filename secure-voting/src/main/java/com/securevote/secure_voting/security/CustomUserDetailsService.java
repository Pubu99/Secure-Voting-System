package com.securevote.secure_voting.security;

import com.securevote.secure_voting.model.User;
import com.securevote.secure_voting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPasswordHash(),
                        Collections.singleton(
                                new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())
                        )
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
