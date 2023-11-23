package com.example.jeeproject.services;

import com.example.jeeproject.dtos.CategoryDTO;
import com.example.jeeproject.entities.Category;
import com.example.jeeproject.entities.TeamMate;
import com.example.jeeproject.entities.Article;
import com.example.jeeproject.helpers.TeamMateHelper;
import com.example.jeeproject.repositories.CategoryRepository;
import com.example.jeeproject.repositories.TeamMateRepository;
import com.example.jeeproject.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TeamMateRepository teamMateRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TeamMateHelper teamMateHelper;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToDto(category);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDto) {
        Category category = convertToEntity(categoryDto);

        if (categoryDto.getTeamMateId() != 0) {
            TeamMate teamMate = teamMateRepository.findById(categoryDto.getTeamMateId())
                    .orElseThrow(() -> new RuntimeException("Member not found with id: " + categoryDto.getTeamMateId()));
            category.setTeamMate(teamMate);
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(categoryDto.getName());

        if (categoryDto.getTeamMateId() != 0) {
            TeamMate teamMate = teamMateRepository.findById(categoryDto.getTeamMateId())
                    .orElseThrow(() -> new RuntimeException("Member not found with id: " + categoryDto.getTeamMateId()));
            category.setTeamMate(teamMate);
        }
        if (categoryDto.getArticleIdList() != null && !categoryDto.getArticleIdList().isEmpty()) {
            List<Article> articles = articleRepository.findAllById(categoryDto.getArticleIdList());
            category.setArticles(articles);
        }

        Category updatedCategory = categoryRepository.save(category);
        return convertToDto(updatedCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO convertToDto(Category category) {
        String roleMember = teamMateHelper.getTeamMateRole();
        boolean isAdminOrMember = Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER");

        List<Long> productIds = category.getArticles() != null
                ? category.getArticles().stream().map(Article::getId).toList()
                : List.of();

        if (isAdminOrMember) {
            return new CategoryDTO(
                    category.getId(),
                    category.getName(),
                    category.getTeamMate().getId(),
                    productIds
            );
        } else {
            return new CategoryDTO(
                    category.getId(),
                    category.getName(),
                    0,
                    productIds
            );
        }
    }


    private Category convertToEntity(CategoryDTO categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());

        TeamMate teamMate = teamMateRepository.findById(categoryDto.getTeamMateId())
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + categoryDto.getTeamMateId()));

        if (teamMate != null) {
            category.setTeamMate(teamMate);
        } else {
            throw new IllegalArgumentException("Member not found with id: " + categoryDto.getTeamMateId());
        }
        return category;
    }
}
