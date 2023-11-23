package com.example.jeeproject.controllers;

import com.example.jeeproject.dtos.CategoryDTO;
import com.example.jeeproject.services.CategoryService;
import com.example.jeeproject.helpers.TeamMateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TeamMateHelper teamMateHelper;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDto) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "COMMERCIAL")) {
            CategoryDTO createdCategory = categoryService.createCategory(categoryDto);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDto) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "COMMERCIAL")) {
            CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDto);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "COMMERCIAL")) {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}