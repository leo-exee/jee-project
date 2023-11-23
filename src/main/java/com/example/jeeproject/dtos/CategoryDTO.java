package com.example.jeeproject.dtos;

import java.util.List;

public class CategoryDTO {

    private long id;
    private String name;
    private long teamMateId;
    private List<Long> articleIdList;

    public CategoryDTO() {
    }

    public CategoryDTO(long id, String name, long teamMateId, List<Long> articleIdList) {
        this.id = id;
        this.name = name;
        this.teamMateId = teamMateId;
        this.articleIdList = articleIdList;
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

    public long getTeamMateId() {
        return teamMateId;
    }

    public void setTeamMateId(long teamMateId) {
        this.teamMateId = teamMateId;
    }

    public List<Long> getArticleIdList() {
        return articleIdList;
    }

    public void setArticleIdList(List<Long> articleIdList) {
        this.articleIdList = articleIdList;
    }
}
