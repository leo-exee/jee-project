package com.example.jeeproject.dtos;
import java.time.LocalDate;
import java.util.List;

public class ClientDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate registrationDate;

    private List<Long> quoteIdList;

    public ClientDTO() {
    }

    public ClientDTO(Long id, String firstName, String lastName, String email, LocalDate registrationDate, List<Long> quoteIdList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.quoteIdList = quoteIdList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Long> getQuoteIdList() {
        return quoteIdList;
    }

    public void setQuoteIdList(List<Long> quoteIdList) {
        this.quoteIdList = quoteIdList;
    }
}
