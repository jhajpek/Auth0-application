package com.example.auth.service;

import com.example.auth.dto.ResultsDto;
import com.example.auth.dto.RoundDto;
import com.example.auth.entity.Round;
import com.example.auth.mapper.RoundMapper;
import com.example.auth.repo.RoundRepository;
import com.example.auth.util.RoundManagementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundDto getLatestRound() {
        Optional<Round> notClosedRound = roundRepository.findFirstByClosedAtIsNull();
        if (notClosedRound.isPresent()) {
            return notClosedRound.map(RoundMapper::mapToRoundDto).get();
        }

        Optional<Round> closedRoundWithoutNumbers = roundRepository.findFirstByClosedAtIsNotNullAndNumbersIsNull();
        if (closedRoundWithoutNumbers.isPresent()) {
            return closedRoundWithoutNumbers.map(RoundMapper::mapToRoundDto).get();
        }

        Optional<Round> closedRoundWithNumbers = roundRepository.findFirstByNumbersIsNotNullOrderByClosedAtDesc();
        if (closedRoundWithNumbers.isPresent()) {
            return closedRoundWithNumbers.map(RoundMapper::mapToRoundDto).get();
        }

        return null;
    }

    public void activateNewRound() {
        Optional<Round> notClosedRound = roundRepository.findFirstByClosedAtIsNull();
        if (notClosedRound.isPresent()) {
            throw new RoundManagementException("Nije moguće otvoriti kolo ako je jedno već u tijeku.");
        }

        Optional<Round> closedRoundWithoutNumbers = roundRepository.findFirstByClosedAtIsNotNullAndNumbersIsNull();
        if (closedRoundWithoutNumbers.isPresent()) {
            throw new RoundManagementException("Nije moguće otvoriti kolo ako je jedno već u tijeku.");
        }

        roundRepository.save(new Round());
    }

    public void closeActiveRound() {
        Optional<Round> notClosedRound = roundRepository.findFirstByClosedAtIsNull();

        if (notClosedRound.isEmpty()) {
            throw new RoundManagementException("Nije moguće zatvoriti trenutačno kolo ako ono ne postoji.");
        }

        Round roundToClose = notClosedRound.get();
        roundToClose.setClosedAt(LocalDateTime.now());
        roundRepository.save(roundToClose);
    }

    public void storeRoundResults(ResultsDto resultsDto) {
        Optional<Round> closedRoundWithNumbers = roundRepository.findFirstByClosedAtIsNotNullAndNumbersIsNull();

        if (closedRoundWithNumbers.isEmpty()) {
            throw new RoundManagementException("Svako kolo već ima spremljene rezultate ili trenutno kolo nije zatvoreno.");
        }

        Round roundToUpdate = closedRoundWithNumbers.get();
        roundToUpdate.setNumbers(resultsDto.getNumbers());
        roundRepository.save(roundToUpdate);
    }

}
