package com.example.jeeproject.entities;

import jakarta.persistence.*;

@Entity
public class QuoteLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estimateId", nullable = false)
    private Quote quote;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId", nullable = false)
    private Article article;

    public QuoteLine() {
    }

    public QuoteLine(float price, long inventory) {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
