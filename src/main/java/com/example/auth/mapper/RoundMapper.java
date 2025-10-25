package com.example.auth.mapper;

import com.example.auth.dto.RoundDto;
import com.example.auth.entity.Round;

public class RoundMapper {

    public static RoundDto mapToRoundDto(Round entity) {
        RoundDto dto = new RoundDto();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setClosedAt(entity.getClosedAt());
        dto.setNumbers(entity.getNumbers());
        return dto;
    }

    public static Round mapToRound(RoundDto dto) {
        Round entity = new Round();
        entity.setId(dto.getId());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setClosedAt(dto.getClosedAt());
        entity.setNumbers(dto.getNumbers());
        return entity;
    }
}
