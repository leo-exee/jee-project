package com.example.jeeproject.services;

import com.example.jeeproject.dtos.TeamMateDTO;
import com.example.jeeproject.entities.Category;
import com.example.jeeproject.entities.Quote;
import com.example.jeeproject.entities.TeamMate;
import com.example.jeeproject.helpers.TeamMateHelper;
import com.example.jeeproject.repositories.CategoryRepository;
import com.example.jeeproject.repositories.QuoteRepository;
import com.example.jeeproject.repositories.TeamMateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamMateService {

    @Autowired
    private TeamMateRepository teamMateRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TeamMateHelper teamMateHelper;

    public List<TeamMateDTO> getAllTeamMates() {
        String roleTeamMate = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleTeamMate, "ADMIN") || Objects.equals(roleTeamMate, "MEMBER")){
            System.out.println("roleTeamMate: " + roleTeamMate);
        }
        List<TeamMate> teamMates = teamMateRepository.findAll();
        return teamMates.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public TeamMateDTO getTeamMateById(Long id) {
        TeamMate teamMate = teamMateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeamMate not found with id: " + id));
        return convertToDto(teamMate);
    }

    public TeamMateDTO createTeamMate(TeamMateDTO teamMateDTO) {
        TeamMate teamMate = convertToEntity(teamMateDTO);
        TeamMate savedTeamMate = teamMateRepository.save(teamMate);
        return convertToDto(savedTeamMate);
    }

    public TeamMateDTO updateTeamMate(Long id, TeamMateDTO teamMateDTO) {
        TeamMate teamMate = teamMateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeamMate not found with id: " + id));

        teamMate.setFirstname(teamMateDTO.getFirstname());
        teamMate.setLastname(teamMateDTO.getLastname());
        teamMate.setRole(teamMateDTO.getRole());
        teamMate.setEmail(teamMateDTO.getEmail());

        if (teamMateDTO.getQuotesIdList() != null && !teamMateDTO.getQuotesIdList().isEmpty()) {
            List<Quote> newQuotes = quoteRepository.findAllById(teamMateDTO.getQuotesIdList());
            teamMate.getQuotes().addAll(newQuotes);
        }

        if (teamMateDTO.getCategoryIdList() != null && !teamMateDTO.getCategoryIdList().isEmpty()) {
            List<Category> newCategories = categoryRepository.findAllById(teamMateDTO.getCategoryIdList());
            teamMate.getCategories().addAll(newCategories);
        }

        TeamMate updatedTeamMate = teamMateRepository.save(teamMate);
        return convertToDto(updatedTeamMate);
    }

    public void deleteTeamMate(Long id) {
        teamMateRepository.deleteById(id);
    }

    private TeamMateDTO convertToDto(TeamMate teamMate) {
        List<Long> estimateIds = Optional.ofNullable(teamMate.getQuotes())
                .orElse(Collections.emptyList())
                .stream()
                .map(Quote::getId)
                .collect(Collectors.toList());

        List<Long> categoryIds = Optional.ofNullable(teamMate.getCategories())
                .orElse(Collections.emptyList())
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        return new TeamMateDTO(
                teamMate.getId(),
                teamMate.getFirstname(),
                teamMate.getLastname(),
                teamMate.getRole(),
                teamMate.getEmail(),
                estimateIds,
                categoryIds
        );
    }

    private TeamMate convertToEntity(TeamMateDTO teamMateDTO) {
        TeamMate teamMate = new TeamMate(
                teamMateDTO.getFirstname(),
                teamMateDTO.getLastname(),
                teamMateDTO.getRole(),
                teamMateDTO.getEmail()
        );

        if (teamMateDTO.getQuotesIdList() != null && !teamMateDTO.getQuotesIdList().isEmpty()) {
            List<Quote> quotes = quoteRepository.findAllById(teamMateDTO.getQuotesIdList());
            teamMate.setQuotes(quotes);
        }

        if (teamMateDTO.getCategoryIdList() != null && !teamMateDTO.getCategoryIdList().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(teamMateDTO.getCategoryIdList());
            teamMate.setCategories(categories);
        }

        return teamMate;
    }

}
