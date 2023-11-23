package com.example.jeeproject.controllers;
import com.example.jeeproject.dtos.TeamMateDTO;
import com.example.jeeproject.helpers.TeamMateHelper;
import com.example.jeeproject.services.TeamMateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/members")
public class TeamMateController {

    @Autowired
    private TeamMateService teamMateService;

    @Autowired
    private TeamMateHelper teamMateHelper;

    @GetMapping
    public ResponseEntity<List<TeamMateDTO>> getAllTeamMates() {
        String roleTeamMate = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleTeamMate, "ADMIN") || Objects.equals(roleTeamMate, "COMMERCIAL")) {
            List<TeamMateDTO> members = teamMateService.getAllTeamMates();
            return new ResponseEntity<>(members, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamMateDTO> getTeamMateById(@PathVariable Long id) {
        String roleTeamMate = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleTeamMate, "ADMIN") || Objects.equals(roleTeamMate, "COMMERCIAL")) {
            TeamMateDTO member = teamMateService.getTeamMateById(id);
            return new ResponseEntity<>(member, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<TeamMateDTO> createTeamMate(@RequestBody TeamMateDTO teamMateDTO) {
        String roleTeamMate = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleTeamMate, "ADMIN") || Objects.equals(roleTeamMate, "COMMERCIAL")) {
            TeamMateDTO createdTeamMate = teamMateService.createTeamMate(teamMateDTO);
            return new ResponseEntity<>(createdTeamMate, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamMateDTO> updateTeamMate(@PathVariable Long id, @RequestBody TeamMateDTO teamMateDTO) {
        String roleTeamMate = teamMateHelper.getTeamMateRole();
        Long idTeamMate = teamMateHelper.getTeamMateId();
        if (Objects.equals(roleTeamMate, "ADMIN") || (Objects.equals(roleTeamMate, "COMMERCIAL") && idTeamMate.equals(teamMateDTO.getId()))) {
            TeamMateDTO updatedTeamMate = teamMateService.updateTeamMate(id, teamMateDTO);
            return new ResponseEntity<>(updatedTeamMate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamMate(@PathVariable Long id) {
        String roleTeamMate = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleTeamMate, "ADMIN")) {
            teamMateService.deleteTeamMate(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}