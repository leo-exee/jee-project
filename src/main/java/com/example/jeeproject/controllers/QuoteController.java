package com.example.jeeproject.controllers;
import com.example.jeeproject.dtos.QuoteDetailsDTO;
import com.example.jeeproject.dtos.QuoteLineDTO;
import com.example.jeeproject.helpers.ClientHelper;
import com.example.jeeproject.services.QuoteService;
import com.example.jeeproject.helpers.TeamMateHelper;
import com.example.jeeproject.dtos.QuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/quotes")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private TeamMateHelper teamMateHelper;

    @Autowired
    private ClientHelper clientHelper;

    @GetMapping
    public ResponseEntity<List<QuoteDetailsDTO>> getAllQuotes(
            @RequestParam(name = "validated", required = false) boolean validated,
            @RequestParam(name = "paid", required = false) boolean paid,
            @RequestParam(name = "canceled", required = false) boolean canceled,
            @RequestParam(name = "before_date", required = false) LocalDate before_date,
            @RequestParam(name = "after_date", required = false) LocalDate after_date,
            @RequestParam(name = "all", required = false) boolean allQuoteMember) {

        String roleMember = teamMateHelper.getTeamMateRole();
        long idMember = teamMateHelper.getTeamMateId();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<QuoteDetailsDTO> quotes = quoteService.getQuotesFiltered(validated, paid, canceled,before_date, after_date, idMember, roleMember.equals("ADMIN"), allQuoteMember);
            return new ResponseEntity<>(quotes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteDetailsDTO> getQuoteById(@PathVariable Long id) {
        String roleMember = teamMateHelper.getTeamMateRole();
        long authenticatedClient = clientHelper.checkClientExists();
        QuoteDetailsDTO quote = quoteService.getQuote(id);
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER") || authenticatedClient == quote.getClientId()) {
            return new ResponseEntity<>(quote, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<QuoteDTO> createQuote(@RequestBody quoteWithLine quoteRequest) {
        QuoteDTO createdQuote = quoteService.createQuoteDetails(quoteRequest.getQuoteDTO(), quoteRequest.getQuoteLinesDTO());
        return new ResponseEntity<>(createdQuote, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<QuoteDTO> updateQuote(@PathVariable Long id, @RequestBody editableQuoteLine editableQuoteLine) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            QuoteDTO updatedQuote = quoteService.updateQuotes(id, editableQuoteLine.getNewQuoteLines(), editableQuoteLine.getOldQuoteLineIdList());
            return new ResponseEntity<>(updatedQuote, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<List<QuoteDTO>> validateQuote(@RequestBody List<Long> quoteIds) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<QuoteDTO> updatedQuotes = quoteService.validateQuotes(quoteIds);
            return new ResponseEntity<>(updatedQuotes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/payed")
    public ResponseEntity<List<QuoteDTO>> payedQuote(@RequestBody List<Long> quoteIds) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<QuoteDTO> updatedQuotes = quoteService.paymentQuotes(quoteIds);
            return new ResponseEntity<>(updatedQuotes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<List<QuoteDTO>> cancelQuote(@RequestBody List<Long> quoteIds) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<QuoteDTO> updatedQuotes = quoteService.cancelQuotes(quoteIds);
            return new ResponseEntity<>(updatedQuotes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuote(@PathVariable Long id) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            quoteService.deleteQuote(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    public static class quoteWithLine {
        private QuoteDTO quoteDTO;
        private List<QuoteLineDTO> quoteLineDetailDTOS;

        public quoteWithLine() {
        }

        public quoteWithLine(QuoteDTO quoteDTO, List<QuoteLineDTO> quoteLineDetailDTOS) {
            this.quoteDTO = quoteDTO;
            this.quoteLineDetailDTOS = quoteLineDetailDTOS;
        }

        public QuoteDTO getQuoteDTO() {
            return quoteDTO;
        }

        public void setQuoteDTO(QuoteDTO quoteDTO) {
            this.quoteDTO = quoteDTO;
        }

        public List<QuoteLineDTO> getQuoteLinesDTO() {
            return quoteLineDetailDTOS;
        }

        public void setQuoteLinesDTO(List<QuoteLineDTO> quoteLineDetailDTOS) {
            this.quoteLineDetailDTOS = quoteLineDetailDTOS;
        }
    }

    public static class editableQuoteLine {
        private List<QuoteLineDTO> QuoteLinesDTO;
        private List<Long> oldQuoteLineIdList;

        public editableQuoteLine() {
        }

        public editableQuoteLine(List<QuoteLineDTO> QuoteLinesDTO, List<Long> oldQuoteLineIdList) {
            this.QuoteLinesDTO = QuoteLinesDTO;
            this.oldQuoteLineIdList = oldQuoteLineIdList;
        }

        public List<QuoteLineDTO> getNewQuoteLines() {
            return QuoteLinesDTO;
        }

        public void setNewQuoteLines(List<QuoteLineDTO> newDTOQuoteLineDetails) {
            this.QuoteLinesDTO = newDTOQuoteLineDetails;
        }

        public List<Long> getOldQuoteLineIdList() {
            return oldQuoteLineIdList;
        }

        public void setOldQuoteLineIdList(List<Long> oldQuoteLineIdList) {
            this.oldQuoteLineIdList = oldQuoteLineIdList;
        }
    }
}