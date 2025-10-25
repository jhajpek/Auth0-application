package com.example.auth.controller;

import com.example.auth.dto.PredictionsDto;
import com.example.auth.dto.RoundDto;
import com.example.auth.dto.TicketDto;
import com.example.auth.service.RoundService;
import com.example.auth.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class ViewController {

    private final RoundService roundService;

    private final TicketService ticketService;

    @GetMapping("/")
    public String getHomePage(Model model, @AuthenticationPrincipal OidcUser principal) {
        RoundDto roundDto = roundService.getLatestRound();
        model.addAttribute("round", roundDto);

        int numberOfTickets = roundDto == null ? 0 : ticketService.getNumberOfTicketsFromRound(roundDto.getId());
        model.addAttribute("numberOfTickets", numberOfTickets);

        if (principal != null) {
            String username = principal.getEmail();
            model.addAttribute("username", username);
            Long roundId = roundDto == null ? -1 : roundDto.getId();
            model.addAttribute("tickets", ticketService.getUsersCurrentTickets(username, roundId));
        } else {
            model.addAttribute("tickets", List.of());
        }

        return "home";
    }

    @GetMapping("/new-ticket")
    public String getNewTicketFormPage(Model model, @AuthenticationPrincipal OidcUser principal) {
        RoundDto roundDto = roundService.getLatestRound();
        if (roundDto == null || roundDto.getClosedAt() != null) {
            return getHomePage(model, principal);
        }
        PredictionsDto predictions = new PredictionsDto();
        model.addAttribute("predictions", predictions);
        return "new-ticket-form";
    }

    @PostMapping("/new-ticket")
    public String createNewTicket(@ModelAttribute PredictionsDto predictions,
                                  RedirectAttributes redirectAttributes,
                                  @AuthenticationPrincipal OidcUser principal) {
        try {
            TicketDto ticketDto = new TicketDto();

            ticketDto.setIdCardNumber(predictions.getIdCardNumber());

            List<Integer> numbers = mapCommaSeparatedNumbersToList(predictions.getCommaSeparatedNumbers());
            ticketDto.setNumbers(numbers);

            RoundDto roundDto = roundService.getLatestRound();
            ticketDto.setRoundDto(roundDto);

            ticketDto.setUsername(principal.getEmail());

            ticketService.createTicket(ticketDto);
            return "redirect:/";
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                String errMsg = "Uneseni brojevi moraju biti razdvojeni isključivo zarezom.";
                redirectAttributes.addFlashAttribute("errMsg", errMsg);
            } else {
                redirectAttributes.addFlashAttribute("errMsg", e.getMessage());
            }
            redirectAttributes.addFlashAttribute("predictions", predictions);
            return "redirect:/new-ticket";
        }
    }

    @GetMapping("/tickets/{ticketId}")
    public String getTicketPage(@PathVariable("ticketId") UUID ticketId, Model model) {
        TicketDto ticketDto = ticketService.getTicketById(ticketId);
        model.addAttribute("ticket", ticketDto);
        return "ticket";
    }

    private List<Integer> mapCommaSeparatedNumbersToList(String commaSeparatedNumbers) {
        String[] numbersAsString = commaSeparatedNumbers.split(",");
        return Arrays.stream(numbersAsString).map(Integer::parseInt).toList();
    }

}
