package com.example.auth.service;

import com.example.auth.dto.TicketDto;
import com.example.auth.entity.Ticket;
import com.example.auth.mapper.TicketMapper;
import com.example.auth.repo.TicketRepository;
import com.example.auth.util.TicketManagementException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TicketService {

    private final String applicationUrl;

    private final TicketRepository ticketRepository;

    public TicketService(@Value("${app.url}") String applicationUrl, TicketRepository ticketRepository) {
        this.applicationUrl = applicationUrl;
        this.ticketRepository = ticketRepository;
    }

    public TicketDto getTicketById(UUID uuid) {
        return ticketRepository.findById(uuid).map(TicketMapper::mapToTicketDto).
                orElseThrow(() -> new TicketManagementException("Ne postoji listić s identifikatorom navedenim u URL-u."));
    }

    public List<TicketDto> getUsersCurrentTickets(String username, Long roundId) {
        return ticketRepository.findByUsernameAndRoundIdOrderByCreatedAtDesc(username, roundId).stream().map(TicketMapper::mapToTicketDto).toList();
    }

    public int getNumberOfTicketsFromRound(Long roundId) {
        return ticketRepository.countByRoundId(roundId);
    }

    public void createTicket(TicketDto ticketDto) {
        checkIfTicketIsValid(ticketDto);
        Ticket ticket = TicketMapper.mapToTicket(ticketDto);
        Ticket savedTicket = ticketRepository.save(ticket);
        setTicketQrCode(savedTicket);
        ticketRepository.save(savedTicket);
    }

    private void checkIfTicketIsValid(TicketDto ticketDto) {
        if (ticketDto.getRoundDto() == null || ticketDto.getRoundDto().getClosedAt() != null) {
            String errMsg = "Trenutno nije moguće uplatiti listić.";
            throw new TicketManagementException(errMsg);
        }

        String errMsg = "";
        int idCardLength = ticketDto.getIdCardNumber().length();
        if (idCardLength == 0 || idCardLength > 20) {
            errMsg += "Duljina broja osobne iskaznice ili putovnice treba biti između 1 i 20 znakova.\n";
        }

        int predictedNumbersSize = ticketDto.getNumbers().size();
        if (predictedNumbersSize < 6 || predictedNumbersSize > 10) {
            errMsg += "Broj predviđenih brojeva mora biti između 6 i 10\n";
        }

        for (Integer number : ticketDto.getNumbers()) {
            if (number < 1 || number > 45) {
                errMsg = String.format("Broj %d nije u očekivanom intervalu([1, 45]).\n", number);
            }
        }

        List<Integer> numbers = ticketDto.getNumbers();
        Set<Integer> numbersSet = new HashSet<>(numbers);
        if (numbers.size() != numbersSet.size()) {
            errMsg += "Ne smiju postojati duplikati među brojevima na listiću.";
        }

        if (!errMsg.isEmpty()) {
            throw new TicketManagementException(errMsg);
        }
    }

    private void setTicketQrCode(Ticket ticket) {
        String text = applicationUrl + "/tickets/" + ticket.getId().toString();
        int width = 250;
        int height = 250;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
            byte[] qrBytes = byteArrayOutputStream.toByteArray();
            String qrBase64 = Base64.getEncoder().encodeToString(qrBytes);
            ticket.setQrBase64(qrBase64);
        } catch (Exception e) {
            throw new TicketManagementException("Greška nastala pri generiranju QR koda.");
        }
    }

}
