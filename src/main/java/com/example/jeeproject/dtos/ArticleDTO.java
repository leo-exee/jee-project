package com.example.jeeproject.dtos;

public class ArticleDTO {

    private long id;
    private String name;
    private String code;
    private long stock;
    private long categoryId;
    private float priceHT;

    public ArticleDTO() {
    }

    public ArticleDTO(long id, String name, String code, long stock, long categoryId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.stock = stock;
        this.categoryId = categoryId;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public float getPriceHT() {
        return priceHT;
    }

    public void setPriceHT(float priceHT) {
        this.priceHT = priceHT;
    }
}