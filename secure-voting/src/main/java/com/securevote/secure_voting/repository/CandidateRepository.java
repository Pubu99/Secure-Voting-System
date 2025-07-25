package com.securevote.secure_voting.repository;

import com.securevote.secure_voting.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}