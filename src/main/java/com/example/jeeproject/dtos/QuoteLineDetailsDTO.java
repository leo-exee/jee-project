package com.example.jeeproject.dtos;

public class QuoteLineDetailsDTO {

    private long id;
    private float priceHT;
    private long quoteId;
    private long articleId;
    private String articleName;

    public QuoteLineDetailsDTO() {
    }

    public QuoteLineDetailsDTO(long id, float priceHT, long quoteId, String articleName) {
        this.id = id;
        this.priceHT = priceHT;
        this.quoteId = quoteId;
        this.articleId = articleId;
        this.articleName = articleName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getPriceHT() {
        return priceHT;
    }

    public void setPriceHT(float priceHT) {
        this.priceHT = priceHT;
    }

    public long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(long quoteId) {
        this.quoteId = quoteId;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }
}
