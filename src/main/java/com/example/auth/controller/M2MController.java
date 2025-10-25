package com.example.auth.controller;

import com.example.auth.dto.ResultsDto;
import com.example.auth.service.RoundService;
import com.example.auth.util.RoundManagementException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class M2MController {

    private final RoundService roundService;

    @PostMapping("/new-round")
    public ResponseEntity<?> activateNewRound() {
        try {
            roundService.activateNewRound();
            return ResponseEntity.noContent().build();
        } catch (RoundManagementException e) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/close")
    public ResponseEntity<?> closeActiveRound() {
        try {
            roundService.closeActiveRound();
            return ResponseEntity.noContent().build();
        } catch (RoundManagementException e) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/store-results")
    public ResponseEntity<?> storeRoundResults(@RequestBody @Valid ResultsDto resultsDto) {
        try {
            roundService.storeRoundResults(resultsDto);
            return ResponseEntity.noContent().build();
        } catch (RoundManagementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}
