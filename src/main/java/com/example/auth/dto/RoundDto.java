package com.example.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoundDto {

    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime closedAt;

    private List<Integer> numbers;

}
