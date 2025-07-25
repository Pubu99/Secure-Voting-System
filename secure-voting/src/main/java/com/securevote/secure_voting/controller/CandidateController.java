package com.securevote.secure_voting.controller;

import com.securevote.secure_voting.model.Candidate;
import com.securevote.secure_voting.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Candidate addCandidate(@RequestBody Candidate candidate) {
        return candidateRepository.save(candidate);
    }
}