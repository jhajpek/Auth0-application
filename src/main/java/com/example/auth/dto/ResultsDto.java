package com.example.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ResultsDto {

    @NotNull
    private List<Integer> numbers;

}
