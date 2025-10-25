package com.example.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class TicketDto {

    private UUID id;

    @NotNull
    @Size(min = 1, max = 20)
    private String idCardNumber;

    private LocalDateTime createdAt;

    @NotNull
    @Size(min = 6, max = 10)
    private List<Integer> numbers;

    @NotNull
    private RoundDto roundDto;

    @NotNull
    private String username;

    @NotNull
    private String qrBase64;

}
