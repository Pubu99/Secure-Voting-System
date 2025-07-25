package com.securevote.secure_voting.controller;

import com.securevote.secure_voting.dto.VoteRequest;
import com.securevote.secure_voting.model.Candidate;
import com.securevote.secure_voting.model.User;
import com.securevote.secure_voting.model.Vote;
import com.securevote.secure_voting.repository.CandidateRepository;
import com.securevote.secure_voting.repository.UserRepository;
import com.securevote.secure_voting.repository.VoteRepository;
import com.securevote.secure_voting.security.JwtUtil;
import com.securevote.secure_voting.security.RSAUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/vote")
@Slf4j
public class VoteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public String submitVote(@RequestHeader("Authorization") String authHeader,
                             @RequestBody VoteRequest voteRequest) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return "Missing or invalid Authorization header";
            }

            String token = authHeader.replace("Bearer ", "").trim();
            String username = jwtUtil.extractUsername(token);

            if (username == null) return "Invalid or expired token";

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) return "User not found";

            User user = userOptional.get();
            if (user.isHasVoted()) return "You have already voted";

            // Validate vote hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(voteRequest.getEncryptedVote().getBytes());
            String computedHash = Base64.getEncoder().encodeToString(hashBytes);

            if (!computedHash.equals(voteRequest.getHash())) {
                return "Vote integrity check failed: hash mismatch";
            }

            // Save vote (anonymously)
            Vote vote = new Vote();
            vote.setEncryptedVote(voteRequest.getEncryptedVote());
            vote.setHash(voteRequest.getHash());
            vote.setTimestamp(LocalDateTime.now());
            voteRepository.save(vote);

            // Mark user as voted
            user.setHasVoted(true);
            userRepository.save(user);

            return "Vote submitted successfully. Your receipt hash: " + vote.getHash();

        } catch (Exception e) {
            log.error("Vote submission failed", e);
            return "Vote submission failed due to server error.";
        }
    }

    @GetMapping("/pubkey")
    public String getPublicKey() {
        try {
            PublicKey publicKey = RSAUtil.getPublicKey();
            return Base64.getEncoder().encodeToString(publicKey.getEncoded());
        } catch (Exception e) {
            log.error("Failed to load public key", e);
            return "Error retrieving public key";
        }
    }

    @GetMapping("/verify/{hash}")
    public boolean verifyVote(@PathVariable String hash) {
        return voteRepository.existsByHash(hash);
    }

    @GetMapping("/status")
    public Map<String, Boolean> getVotingStatus(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing or invalid Authorization header");
            }

            String token = authHeader.replace("Bearer ", "").trim();
            String username = jwtUtil.extractUsername(token);

            if (username == null) {
                throw new IllegalArgumentException("Invalid or expired token");
            }

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                throw new IllegalArgumentException("User not found");
            }

            User user = userOptional.get();
            Map<String, Boolean> response = new HashMap<>();
            response.put("hasVoted", user.isHasVoted());
            return response;
        } catch (Exception e) {
            log.error("Failed to retrieve voting status", e);
            throw new RuntimeException("Error retrieving voting status");
        }
    }

    @GetMapping("/results")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Integer> tallyVotes() {
        Map<String, Integer> result = new HashMap<>();

        try {
            PrivateKey privateKey = RSAUtil.getPrivateKey();
            List<Vote> votes = voteRepository.findAll();
            List<Candidate> validCandidates = candidateRepository.findAll();
            Set<String> validCandidateNames = new HashSet<>();
            for (Candidate candidate : validCandidates) {
                validCandidateNames.add(candidate.getName());
            }

            for (Vote vote : votes) {
                String decrypted = RSAUtil.decrypt(vote.getEncryptedVote(), privateKey);
                if (validCandidateNames.contains(decrypted)) {
                    result.put(decrypted, result.getOrDefault(decrypted, 0) + 1);
                } else {
                    log.warn("Invalid vote detected for candidate: {}", decrypted);
                }
            }

            return result;

        } catch (Exception e) {
            log.error("Error tallying votes", e);
            throw new RuntimeException("Error tallying votes");
        }
    }
}