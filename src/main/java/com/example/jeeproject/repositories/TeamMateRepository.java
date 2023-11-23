package com.example.jeeproject.repositories;
import com.example.jeeproject.entities.TeamMate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMateRepository extends JpaRepository<TeamMate, Long> {
    TeamMate findByRole(String role);
}
