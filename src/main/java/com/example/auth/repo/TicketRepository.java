package com.example.auth.repo;

import com.example.auth.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    int countByRoundId(Long roundId);
    List<Ticket> findByUsernameAndRoundIdOrderByCreatedAtDesc(String username, Long roundId);

}
