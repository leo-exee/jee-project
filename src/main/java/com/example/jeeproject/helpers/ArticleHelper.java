package com.example.jeeproject.helpers;

import com.example.jeeproject.services.ArticleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class ArticleHelper {
    @Value("${articlePrice}")
    private String url;

    private final Logger logger = LoggerFactory.getLogger(ArticleService.class);
    public float getArticlePrice(long articleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Random random = new Random();
        float randomPrice = 200 + random.nextFloat() * (1000 - 200);
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedPrice = df.format(randomPrice);

        String jsonBody = "{\"articlePrice\":\"" + formattedPrice + "\"}";

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
            String articlePrice = jsonNode.path("json").path("articlePrice").asText();
            String articlePriceFormatted = articlePrice.replace(",", ".");
            return Float.parseFloat(articlePriceFormatted);
        } catch (Exception e) {
            logger.error("An error occurred while parsing JSON", e);
        }
        return -1;
    }

    public Map<Long, Float> getArticlesPrices(List<Long> articleIdList) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<ArticlePrice> articlesPrices = new ArrayList<>();
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("#.##");

        for (Long articleId : articleIdList) {
            float randomPrice = 200 + random.nextFloat() * (1000 - 200);
            String formattedPrice = df.format(randomPrice);
            formattedPrice = formattedPrice.replace(",", ".");
            ArticlePrice articlePrice = new ArticlePrice(articleId, Float.parseFloat(formattedPrice));
            articlesPrices.add(articlePrice);
        }

        ArticlesPrices articlePriceRequest = new ArticlesPrices(articlesPrices);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(articlePriceRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("An error occurred while parsing JSON : " + e);
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
            JsonNode articlePricesNode = jsonNode.path("json").path("articlesPrices");
            List<ArticlePrice> responseArticlePrices = objectMapper.readValue(articlePricesNode.toString(),
                    new TypeReference<List<ArticlePrice>>() {});
            Map<Long, Float> pricesMap = new HashMap<>();
            for (ArticlePrice responseArticlePrice : responseArticlePrices) {
                pricesMap.put(responseArticlePrice.getId(), responseArticlePrice.getPrice());
            }
            return pricesMap;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while parsing JSON : " + e);
        }
    }


    public static class ArticlesPrices {
        private List<ArticlePrice> articlePrices;

        public ArticlesPrices(List<ArticlePrice> articlePrices) {
            this.articlePrices = articlePrices;
        }

        public List<ArticlePrice> getProductPrices() {
            return articlePrices;
        }

        public void setProductPrices(List<ArticlePrice> articlePrices) {
            this.articlePrices = articlePrices;
        }
    }

    public static class ArticlePrice {
        private Long id;
        private Float price;

        public ArticlePrice() {
        }

        public ArticlePrice(Long id, Float price) {
            this.id = id;
            this.price = price;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }
    }
}
