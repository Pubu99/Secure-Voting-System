package com.securevote.secure_voting.repository;

import com.securevote.secure_voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByHash(String hash);
}
