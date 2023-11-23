package com.example.jeeproject.services;
import com.example.jeeproject.dtos.QuoteLineDTO;
import com.example.jeeproject.entities.Quote;
import com.example.jeeproject.entities.Article;
import com.example.jeeproject.entities.QuoteLine;
import com.example.jeeproject.repositories.QuoteRepository;
import com.example.jeeproject.repositories.ArticleRepository;
import com.example.jeeproject.repositories.QuoteLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteLineService {

    @Autowired
    private QuoteLineRepository quoteLineRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public List<QuoteLineDTO> getQuoteLines() {
        List<QuoteLine> quoteLines = quoteLineRepository.findAll();
        return quoteLines.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public QuoteLineDTO getQuoteLine(Long id) {
        QuoteLine quoteLine = quoteLineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductXEstimate not found with id: " + id));
        return convertToDto(quoteLine);
    }

    public QuoteLineDTO createQuoteLine(QuoteLineDTO quoteLineDTO) {
        QuoteLine quoteLine = convertToEntity(quoteLineDTO);
        QuoteLine savedQuoteLine = quoteLineRepository.save(quoteLine);
        return convertToDto(savedQuoteLine);
    }

    private QuoteLineDTO convertToDto(QuoteLine quoteLine) {
        return new QuoteLineDTO(
                quoteLine.getId(),
                quoteLine.getQuote().getId(),
                quoteLine.getArticle().getId()
        );
    }

    private QuoteLine convertToEntity(QuoteLineDTO quoteLineDTO) {
        QuoteLine quoteLine = new QuoteLine();

        if (quoteLineDTO.getQuoteId() != 0) {
            Quote quote = quoteRepository.findById(quoteLineDTO.getQuoteId())
                    .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + quoteLineDTO.getQuoteId()));
            quoteLine.setQuote(quote);
        }

        if (quoteLineDTO.getArticleId() != 0) {
            Article article = articleRepository.findById(quoteLineDTO.getArticleId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + quoteLineDTO.getQuoteId()));
            quoteLine.setArticle(article);
        }

        return quoteLine;
    }
}
