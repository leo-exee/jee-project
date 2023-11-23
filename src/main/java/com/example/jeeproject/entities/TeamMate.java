package com.example.jeeproject.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class TeamMate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private String email;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = Quote.class, mappedBy = "teamMate")
    private List<Quote> quotes;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = Category.class, mappedBy = "teamMate")
    private List<Category> categories;

    public TeamMate() {
    }

    public TeamMate(String firstname, String lastname, String role, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.email = email;
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

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
