package com.example.jeeproject.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column
    private LocalDate validationDate;

    @Column
    private LocalDate paymentDate;

    @Column
    private LocalDate cancelDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId")
    private Client client;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = QuoteLine.class, mappedBy = "estimate")
    private List<QuoteLine> quoteLines;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamMateId")
    private TeamMate teamMate;

    public Quote() {
    }

    public Quote(LocalDate creationDate, LocalDate validationDate, LocalDate paymentDate, LocalDate cancelDate) {
        this.creationDate = creationDate;
        this.validationDate = validationDate;
        this.paymentDate = paymentDate;
        this.cancelDate = cancelDate;
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

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public List<QuoteLine> getQuoteLines() {
        return quoteLines;
    }
    public void setQuoteLines(List<QuoteLine> quoteLines) {
        this.quoteLines = quoteLines;
    }

    public TeamMate getTeamMate() {
        return teamMate;
    }
    public void setMember(TeamMate teamMate) {
        this.teamMate = teamMate;
    }

}
