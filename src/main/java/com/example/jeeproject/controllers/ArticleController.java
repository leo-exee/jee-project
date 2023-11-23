package com.example.jeeproject.controllers;
import com.example.jeeproject.dtos.ArticleDTO;
import com.example.jeeproject.helpers.TeamMateHelper;
import com.example.jeeproject.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private TeamMateHelper teamMateHelper;

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles(
            @RequestParam(name = "start", required = false) String startStr,
            @RequestParam(name = "end", required = false) String endStr,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "category", required = false) String category) {
        List<ArticleDTO> products = articleService.getArticlesFiltered(startStr, endStr, name,code, category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        ArticleDTO product = articleService.getArticle(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            ArticleDTO createdArticle = articleService.createArticle(articleDTO);
            return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            ArticleDTO updatedArticle = articleService.updateArticle(id, articleDTO);
            return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN")) {
            articleService.deleteArticle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}