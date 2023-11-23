package com.example.jeeproject.dtos;

import java.time.LocalDate;
import java.util.List;

public class QuoteDTO {

    private Long id;
    private LocalDate creationDate;
    private LocalDate validationDate;
    private LocalDate paymentDate;
    private LocalDate cancelDate;
    private Long clientId;
    private List<Long> quoteLineIdList;
    private Long teamMateId;

    public QuoteDTO() {
    }

    public QuoteDTO(Long id, LocalDate creationDate, LocalDate validationDate, LocalDate paymentDate, LocalDate cancelDate, Long clientId, List<Long> quoteLineIdList, Long teamMateId) {
        this.id = id;
        this.creationDate = creationDate;
        this.validationDate = validationDate;
        this.paymentDate = paymentDate;
        this.cancelDate = cancelDate;
        this.clientId = clientId;
        this.quoteLineIdList = quoteLineIdList;
        this.teamMateId = teamMateId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(LocalDate validationDate) {
        this.validationDate = validationDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<Long> getQuoteLineIdList() {
        return quoteLineIdList;
    }

    public void setQuoteLineIdList(List<Long> quoteLineIdList) {
        this.quoteLineIdList = quoteLineIdList;
    }

    public Long getTeamMateId() {
        return teamMateId;
    }

    public void setTeamMateId(Long teamMateId) {
        this.teamMateId = teamMateId;
    }
}
