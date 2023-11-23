package com.example.jeeproject.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamMateId")
    private TeamMate teamMate;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = Article.class, mappedBy = "category")
    private List<Article> articles;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamMate getTeamMate() {
        return teamMate;
    }
    public void setTeamMate(TeamMate teamMate) {
        this.teamMate = teamMate;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}