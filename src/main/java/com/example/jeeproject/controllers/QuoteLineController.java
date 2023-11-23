package com.example.jeeproject.controllers;
import com.example.jeeproject.dtos.QuoteLineDTO;
import com.example.jeeproject.services.QuoteLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quote-lines")
public class QuoteLineController {

    @Autowired
    private QuoteLineService quoteLineService;

    @GetMapping
    public ResponseEntity<List<QuoteLineDTO>> getAllProductXEstimates() {
        List<QuoteLineDTO> productXEstimates = quoteLineService.getQuoteLines();
        return new ResponseEntity<>(productXEstimates, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteLineDTO> getProductXEstimateById(@PathVariable Long id) {
        QuoteLineDTO productXEstimate = quoteLineService.getQuoteLine(id);
        return new ResponseEntity<>(productXEstimate, HttpStatus.OK);
    }
}
