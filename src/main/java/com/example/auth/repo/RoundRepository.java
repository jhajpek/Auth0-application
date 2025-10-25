package com.example.auth.repo;

import com.example.auth.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {

    Optional<Round> findFirstByClosedAtIsNull();
    Optional<Round> findFirstByClosedAtIsNotNullAndNumbersIsNull();
    Optional<Round> findFirstByNumbersIsNotNullOrderByClosedAtDesc();

}
