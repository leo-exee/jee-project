package com.example.jeeproject.dtos;
import java.util.List;

public class TeamMateDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String role;
    private String email;
    private List<Long> quotesIdList;
    private List<Long> categoryIdList;

    public TeamMateDTO() {
    }

    public TeamMateDTO(Long id, String firstname, String lastname, String role, String email, List<Long> quotesIdList, List<Long> categoryIdList) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.email = email;
        this.quotesIdList = quotesIdList;
        this.categoryIdList = categoryIdList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getQuotesIdList() {
        return quotesIdList;
    }

    public void setQuotesIdList(List<Long> quotesIdList) {
        this.quotesIdList = quotesIdList;
    }

    public List<Long> getCategoryIdList() {
        return categoryIdList;
    }

    public void setCategoryIdList(List<Long> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }
}
