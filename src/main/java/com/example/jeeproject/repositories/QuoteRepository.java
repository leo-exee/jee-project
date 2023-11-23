package com.example.jeeproject.repositories;
import com.example.jeeproject.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("SELECT e FROM Quote e WHERE e.id IN :ids")
    List<Quote> findByIds(@Param("ids") List<Long> ids);
}
