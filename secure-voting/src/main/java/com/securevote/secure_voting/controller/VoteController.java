package com.securevote.secure_voting.controller;

import com.securevote.secure_voting.dto.VoteRequest;
import com.securevote.secure_voting.model.User;
import com.securevote.secure_voting.model.Vote;
import com.securevote.secure_voting.repository.UserRepository;
import com.securevote.secure_voting.repository.VoteRepository;
import com.securevote.secure_voting.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/cast")
    public String castVote(@RequestHeader("Authorization") String token,
                           @RequestBody VoteRequest voteRequest) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username);
        if (user == null || user.isHasVoted()) {
            return "Unauthorized or already voted";
        }

        Vote vote = new Vote();
        vote.setEncryptedVote(voteRequest.getEncryptedVote());
        vote.setHash(voteRequest.getHash());
        voteRepository.save(vote);

        user.setHasVoted(true);
        userRepository.save(user);

        return "Vote cast successfully!";
    }
}
