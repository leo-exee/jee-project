package com.example.jeeproject.services;
import com.example.jeeproject.dtos.ArticleDTO;
import com.example.jeeproject.entities.Category;
import com.example.jeeproject.entities.Article;
import com.example.jeeproject.helpers.ArticleHelper;
import com.example.jeeproject.repositories.CategoryRepository;
import com.example.jeeproject.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleHelper articleHelper;

    public List<ArticleDTO> getArticlesFiltered(String startStr, String endStr, String name, String code, String category) {
        List<Article> articles = articleRepository.findAll();
        List<Long> articleIdList = articles.stream()
                .map(Article::getId)
                .filter(id -> filterByIdRange(id, startStr, endStr))
                .filter(id -> filterByName(articles, id, name))
                .filter(id -> filterByCode(articles, id, code))
                .filter(id -> filterByCategory(articles, id, category))
                .collect(Collectors.toList());

        Map<Long, Float> productPricesMap = articleHelper.getArticlesPrices(articleIdList);

        return articles.stream()
                .filter(product -> articleIdList.contains(product.getId()))
                .map(product -> convertToDtoWithPrice(product, productPricesMap.get(product.getId())))
                .collect(Collectors.toList());
    }

    private boolean filterByIdRange(Long id, String startStr, String endStr) {
        if (startStr != null && !startStr.isEmpty()) {
            Long startId = Long.parseLong(startStr);
            if (id < startId) {
                return false;
            }
        }
        if (endStr != null && !endStr.isEmpty()) {
            Long endId = Long.parseLong(endStr);
            return id <= endId;
        }
        return true;
    }

    private boolean filterByName(List<Article> articles, long id, String name) {
        if (name != null && !name.isEmpty()) {
            for (Article article : articles) {
                if (article.getId() == id && article.getName().toLowerCase().contains(name.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean filterByCode(List<Article> articles, long id, String code) {
        if (code != null && !code.isEmpty()) {
            for (Article article : articles) {
                if (article.getId() == id && article.getCode().contains(code)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean filterByCategory(List<Article> articles, long id, String categoryId) {
        if (categoryId != null && !categoryId.isEmpty()) {
            long category = Long.parseLong(categoryId);
            for (Article article : articles) {
                if (article.getId() == id && article.getCategory().getId() == category) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public ArticleDTO getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        ArticleDTO articleDTO = convertToDto(article);
        articleDTO.setPriceHT(articleHelper.getArticlePrice(article.getId()));
        return articleDTO;
    }

    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        Article article = convertToEntity(articleDTO);
        Article savedArticle = articleRepository.save(article);
        ArticleDTO savedArticleDTO = convertToDto(savedArticle);
        savedArticleDTO.setPriceHT(articleHelper.getArticlePrice(article.getId()));
        return savedArticleDTO;
    }

    public ArticleDTO updateArticle(Long id, ArticleDTO articleDTO) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        article.setName(articleDTO.getName());
        article.setCode(articleDTO.getCode());
        article.setStock(articleDTO.getStock());

        if (articleDTO.getCategoryId() != 0) {
            Category category = categoryRepository.findById(articleDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + articleDTO.getCategoryId()));
            article.setCategory(category);
        }

        Article updatedArticle = articleRepository.save(article);
        ArticleDTO updatedArticleDTO = convertToDto(updatedArticle);
        updatedArticleDTO.setPriceHT(articleHelper.getArticlePrice(article.getId()));
        return updatedArticleDTO;
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    private ArticleDTO convertToDto(Article article) {
        return new ArticleDTO(
                article.getId(),
                article.getName(),
                article.getCode(),
                article.getStock(),
                article.getCategory().getId()
        );
    }

    private ArticleDTO convertToDtoWithPrice(Article article, Float price) {
        ArticleDTO articleDTO = new ArticleDTO(
                article.getId(),
                article.getName(),
                article.getCode(),
                article.getStock(),
                article.getCategory().getId()
        );
        articleDTO.setPriceHT(price);
        return articleDTO;
    }

    private Article convertToEntity(ArticleDTO articleDTO) {
        Article article = new Article(
                articleDTO.getName(),
                articleDTO.getCode(),
                articleDTO.getStock()
        );

        if (articleDTO.getCategoryId() != 0) {
            Category category = categoryRepository.findById(articleDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + articleDTO.getCategoryId()));
            article.setCategory(category);
        }

        return article;
    }

}