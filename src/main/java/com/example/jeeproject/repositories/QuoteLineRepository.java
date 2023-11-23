package com.example.jeeproject.repositories;

import com.example.jeeproject.entities.QuoteLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteLineRepository extends JpaRepository<QuoteLine, Long> {

}
