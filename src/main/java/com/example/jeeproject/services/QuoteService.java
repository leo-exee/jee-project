package com.example.jeeproject.services;
import com.example.jeeproject.dtos.*;
import com.example.jeeproject.entities.*;
import com.example.jeeproject.helpers.ArticleHelper;
import com.example.jeeproject.helpers.TeamMateHelper;
import com.example.jeeproject.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuoteService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private QuoteLineRepository quoteLineRepository;

    @Autowired
    private TeamMateRepository teamMateRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuoteLineService quoteLineService;

    @Autowired
    private ArticleHelper articleHelper;

    @Autowired
    private TeamMateHelper teamMateHelper;

    public List<QuoteDetailsDTO> getQuotesFiltered(boolean validated, boolean paid, boolean canceled, LocalDate before_date, LocalDate after_date, long idMember, boolean isAdmin, boolean byTeamMate) {
        List<Quote> quotes = quoteRepository.findAll();

        Stream<Quote> quotesFiltered = quotes.stream()
                .filter(quote -> (!validated || quote.getValidationDate() != null))
                .filter(quote -> (!paid || quote.getPaymentDate() != null))
                .filter(quote -> (!canceled || quote.getCancelDate() != null))
                .filter(quote -> before_date == null || quote.getCreationDate().isBefore(before_date))
                .filter(quote -> after_date == null || quote.getCreationDate().isAfter(after_date));
        if (!isAdmin || !byTeamMate) {
            quotesFiltered = quotesFiltered.filter(quote -> quote.getTeamMate().getId() == idMember);
        }
        return quotesFiltered.map(this::convertToDetailsDto).collect(Collectors.toList());
    }

    public QuoteDetailsDTO getQuote(long id) {
        Quote quote = quoteRepository.findById(id).orElseThrow(() -> new RuntimeException("Quote not found with id: " + id));;
        return convertToDetailsDto(quote);
    }

    public QuoteDTO createQuoteDetails(QuoteDTO quoteDTO, List<QuoteLineDTO> quoteLineDetailDTOS) {
        long categoryId = getRecurrentCategory(quoteLineDetailDTOS);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        Quote quote = convertToEntity(quoteDTO);
        quote.setCreationDate(LocalDate.now());
        quote.setMember(category.getTeamMate());
        Quote savedQuote = quoteRepository.save(quote);

        QuoteDTO savedQuoteDTO = convertToDto(savedQuote);
        float totalPrice = 0;
        for (QuoteLineDTO quoteLineDTO : quoteLineDetailDTOS) {
            quoteLineDTO.setQuoteId(savedQuote.getId());
            QuoteLineDTO newQuoteLineDTO = quoteLineService.createQuoteLine(quoteLineDTO);

            savedQuoteDTO.getQuoteLineIdList().add(newQuoteLineDTO.getId());

            Article article = articleRepository.findById(quoteLineDTO.getArticleId())
                    .orElseThrow(() -> new RuntimeException("Article not found with id: " + quoteLineDTO.getArticleId()));
            articleRepository.save(article);
        }

        if (totalPrice > 10000) {
            TeamMate adminTeamMate = teamMateRepository.findByRole("ADMIN");

            if (adminTeamMate == null) {
                throw new RuntimeException("Admin member not found");
            }

            savedQuote.setMember(adminTeamMate);
            quoteRepository.save(savedQuote);
            savedQuoteDTO.setTeamMateId(adminTeamMate.getId());
        }

        return savedQuoteDTO;
    }

    private Long getRecurrentCategory(List<QuoteLineDTO> quoteLineDetailDTOS) {
        Map<Long, Long> categoryInventorySum = new HashMap<>();
        Long maxCategoryId = null;
        long maxInventorySum = Long.MIN_VALUE;

        for (QuoteLineDTO quoteLineDTO : quoteLineDetailDTOS) {
            Article article = articleRepository.findById(quoteLineDTO.getArticleId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + quoteLineDTO.getArticleId()));

            if (article == null || article.getStock() < 0) {
                throw new RuntimeException("Product not available with sufficient stock.");
            }

            Long categoryId = article.getCategory().getId();
            long currentInventorySum = categoryInventorySum.getOrDefault(categoryId, 0L) + 1;
            categoryInventorySum.put(categoryId, currentInventorySum);

            if (currentInventorySum > maxInventorySum) {
                maxInventorySum = currentInventorySum;
                maxCategoryId = categoryId;
            }
        }
        return maxCategoryId;
    }

    public List<QuoteDTO> validateQuotes(List<Long> quoteIdList) {
        List<Quote> quotes = quoteRepository.findByIds(quoteIdList);
        if (quotes.isEmpty()) {
            throw new RuntimeException("Quotes not found with ids: " + quoteIdList);
        }

        String roleMember = teamMateHelper.getTeamMateRole();
        Long idMember = teamMateHelper.getTeamMateId();
        for (Quote quote : quotes) {
            if ((Objects.equals(roleMember, "ADMIN") || idMember.equals(quote.getTeamMate().getId())) && quote.getValidationDate() == null) {
                quote.setValidationDate(LocalDate.now());
                quoteRepository.save(quote);
            }else {
                throw new RuntimeException("Invalid member to update this estimate");
            }
        }

        return convertToDtoList(quotes);
    }

    public List<QuoteDTO> paymentQuotes(List<Long> quoteIdList) {
        List<Quote> quotes = quoteRepository.findByIds(quoteIdList);
        if (quotes.isEmpty()) {
            throw new RuntimeException("Quotes not found with ids: " + quoteIdList);
        }

        String teamMateRole = teamMateHelper.getTeamMateRole();
        Long teamMateId = teamMateHelper.getTeamMateId();
        for (Quote quote : quotes) {
            if ((Objects.equals(teamMateRole, "ADMIN") || teamMateId.equals(quote.getTeamMate().getId())) && quote.getPaymentDate() == null && quote.getValidationDate() != null) {
                quote.setPaymentDate(LocalDate.now());
                quoteRepository.save(quote);
            }else {
                throw new RuntimeException("Invalid team mate to update this quote");
            }
        }

        return convertToDtoList(quotes);
    }

    public List<QuoteDTO> cancelQuotes(List<Long> quoteIdList) {
        List<Quote> quotes = quoteRepository.findByIds(quoteIdList);
        if (quotes.isEmpty()) {
            throw new RuntimeException("Quotes not found with ids: " + quoteIdList);
        }

        String teamMateRole = teamMateHelper.getTeamMateRole();
        Long teamMateId = teamMateHelper.getTeamMateId();
        for (Quote quote : quotes) {
            if ((Objects.equals(teamMateRole, "ADMIN") || teamMateId.equals(quote.getTeamMate().getId())) && quote.getCancelDate() == null) {
                quote.setCancelDate(LocalDate.now());
                for (QuoteLine quoteLine : quote.getQuoteLines()) {
                    Article article = quoteLine.getArticle();
                    articleRepository.save(article);
                }
                quoteRepository.save(quote);
            }else {
                throw new RuntimeException("Invalid team mate to update this quote");
            }
        }

        return convertToDtoList(quotes);
    }

    public QuoteDTO updateQuotes(Long id, List<QuoteLineDTO> newDTOQuoteLineDetails, List<Long> quoteLineIdList) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found with id: " + id));

        if (quote.getValidationDate() == null){
            List<Long> currentQuoteLineIdList = quote.getQuoteLines()
                    .stream()
                    .map(QuoteLine::getId)
                    .toList();

            List<Long> valideQuoteLineIdList = quoteLineIdList
                    .stream()
                    .filter(currentQuoteLineIdList::contains)
                    .toList();

            for (Long quoteLineId : valideQuoteLineIdList) {
                QuoteLine quoteLine = quoteLineRepository.findById(quoteLineId)
                        .orElseThrow(() -> new RuntimeException("Quote line not found with id: " + quoteLineId));
                Article article = quoteLine.getArticle();
                quoteLineRepository.deleteById(quoteLineId);
                articleRepository.save(article);
            }

            for (QuoteLineDTO newQuoteLineDTO : newDTOQuoteLineDetails) {
                Long quoteLineId = newQuoteLineDTO.getId();
                if (currentQuoteLineIdList.contains(quoteLineId)) {
                    QuoteLine existingQuoteLine = quoteLineRepository.findById(quoteLineId)
                            .orElseThrow(() -> new RuntimeException("Quote line not found with id: " + quoteLineId));
                    Article article = existingQuoteLine.getArticle();
                    quoteLineRepository.save(existingQuoteLine);
                    articleRepository.save(article);
                }
            }

            return convertToDto(quote);
        }else {
            throw new RuntimeException("No update after setting ValidationDate");
        }

    }

    public void deleteQuote(Long id) {
        Optional<Quote> optionalEstimate = quoteRepository.findById(id);
        if (optionalEstimate.isPresent()) {
            Quote quote = optionalEstimate.get();
            List<QuoteLine> quoteLines = quote.getQuoteLines();
            if (quote.getPaymentDate() != null){
                for (QuoteLine quoteLine : quoteLines) {
                    Article article = quoteLine.getArticle();
                    articleRepository.save(article);
                    quoteLineRepository.deleteById(quoteLine.getId());
                }
            }else {
                for (QuoteLine quoteLine : quoteLines) {
                    quoteLineRepository.deleteById(quoteLine.getId());
                }
            }
            quoteRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Quote with ID " + id + " not found");
        }
    }

    private QuoteDTO convertToDto(Quote quote) {
        List<Long> quoteLineIdList = new ArrayList<>();

        if (quote.getQuoteLines() != null) {
            quoteLineIdList = quote.getQuoteLines()
                    .stream()
                    .map(QuoteLine::getId)
                    .collect(Collectors.toList());
        }
        Long clientId = (quote.getClient() != null) ? quote.getClient().getId() : null;
        return new QuoteDTO(
                quote.getId(),
                quote.getCreationDate(),
                quote.getValidationDate(),
                quote.getPaymentDate(),
                quote.getCancelDate(),
                clientId,
                quoteLineIdList,
                quote.getTeamMate().getId()
        );
    }

    public List<QuoteDTO> convertToDtoList(List<Quote> quotes) {
        return quotes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private QuoteDetailsDTO convertToDetailsDto(Quote quote) {
        List<QuoteLineDetailsDTO> quoteLineDetailsDTOS = new ArrayList<>();

        if (quote.getQuoteLines() != null) {
            quoteLineDetailsDTOS = quote.getQuoteLines()
                    .stream()
                    .map(this::convertToDevelopedProductXEstimateDto)
                    .collect(Collectors.toList());
        }

        Long clientId = (quote.getClient() != null) ? quote.getClient().getId() : null;

        return new QuoteDetailsDTO(
                quote.getId(),
                quote.getCreationDate(),
                quote.getValidationDate(),
                quote.getPaymentDate(),
                quote.getCancelDate(),
                clientId,
                quoteLineDetailsDTOS,
                quote.getTeamMate().getId()
        );
    }

    private QuoteLineDetailsDTO convertToDevelopedProductXEstimateDto(QuoteLine quoteLine) {
        return new QuoteLineDetailsDTO(
                quoteLine.getId(),
                quoteLine.getQuote().getId(),
                quoteLine.getArticle().getId(),
                quoteLine.getArticle().getName()
        );
    }



    private Quote convertToEntity(QuoteDTO quoteDTO) {
        Quote quote = new Quote(
                quoteDTO.getCreationDate(),
                quoteDTO.getValidationDate(),
                quoteDTO.getPaymentDate(),
                quoteDTO.getCancelDate()
        );

        if (quoteDTO.getClientId() != 0) {
            Client client = clientRepository.findById(quoteDTO.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + quoteDTO.getClientId()));
            quote.setClient(client);
        }

        if (quoteDTO.getTeamMateId() != 0) {
            TeamMate teamMate = teamMateRepository.findById(quoteDTO.getTeamMateId())
                    .orElseThrow(() -> new RuntimeException("Team mate not found with id: " + quoteDTO.getTeamMateId()));
            quote.setMember(teamMate);
        }

        if (quoteDTO.getQuoteLineIdList() != null && !quoteDTO.getQuoteLineIdList().isEmpty()) {
            List<QuoteLine> productsXEstimates = quoteLineRepository.findAllById(quoteDTO.getQuoteLineIdList());
            quote.setQuoteLines(productsXEstimates);
        }

        return quote;
    }

}
