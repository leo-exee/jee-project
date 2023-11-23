package com.example.jeeproject.dtos;

public class QuoteLineDTO {

    private long id;
    private long quoteId;
    private long articleId;

    public QuoteLineDTO() {
    }

    public QuoteLineDTO(long id, long quoteId, long articleId) {
        this.id = id;
        this.quoteId = quoteId;
        this.articleId = articleId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
