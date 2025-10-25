package com.example.auth.mapper;

import com.example.auth.dto.TicketDto;
import com.example.auth.entity.Ticket;

public class TicketMapper {

    public static TicketDto mapToTicketDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticket.getId());
        ticketDto.setCreatedAt(ticket.getCreatedAt());
        ticketDto.setIdCardNumber(ticket.getIdCardNumber());
        ticketDto.setNumbers(ticket.getNumbers());
        ticketDto.setRoundDto(RoundMapper.mapToRoundDto(ticket.getRound()));
        ticketDto.setUsername(ticket.getUsername());
        ticketDto.setQrBase64(ticket.getQrBase64());
        return ticketDto;
    }

    public static Ticket mapToTicket(TicketDto ticketDto) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketDto.getId());
        ticket.setCreatedAt(ticket.getCreatedAt());
        ticket.setIdCardNumber(ticketDto.getIdCardNumber());
        ticket.setNumbers(ticketDto.getNumbers());
        ticket.setRound(RoundMapper.mapToRound(ticketDto.getRoundDto()));
        ticket.setUsername(ticketDto.getUsername());
        ticket.setQrBase64(ticketDto.getQrBase64());
        return ticket;
    }
}
