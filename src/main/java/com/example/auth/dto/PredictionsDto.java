package com.example.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PredictionsDto {

    @NotNull
    @Size(min = 1, max = 20)
    private String idCardNumber;

    @NotNull
    @Size(min = 11, max = 29)
    private String commaSeparatedNumbers;

}
