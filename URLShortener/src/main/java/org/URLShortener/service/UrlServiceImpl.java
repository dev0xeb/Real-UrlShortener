package org.URLShortener.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.URLShortener.data.UrlEntity;
import org.URLShortener.dtos.UrlRequest;
import org.URLShortener.dtos.UrlResponse;
import org.URLShortener.repository.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class UrlServiceImpl implements URLService{
    @Autowired
    private UrlRepo urlRepo;

    private static final String API_URL = "https://api-ssl.bitly.com/v4/shorten";
    private static final String API_TOKEN= "720b8f2d2b819b9bd300ff2be73e4c40ef85e70a";

    @Override
    public UrlResponse createShortUrl(UrlRequest urlRequest) {
        String longUrl = urlRequest.getLongUrl();
        UrlResponse existingUrl = urlRepo.findByLongUrl(longUrl);
        if (existingUrl != null) {
            return existingUrl;
        }

        try {
            String shortUrl = generateShortUrl(longUrl);
            UrlResponse urlResponse = new UrlResponse();
            urlResponse.setShortUrl(shortUrl);
            urlResponse.setLongUrl(longUrl);

            UrlEntity urlEntity = new UrlEntity();
            urlEntity.setLongUrl(longUrl);
            urlEntity.setShortUrl(shortUrl);
            urlRepo.save(urlEntity);
            return urlResponse;
        } catch (Exception createShortUrlException){
            System.out.println("Error creating short URL" + createShortUrlException.getMessage());
            throw new IllegalArgumentException("Invalid Long Url", createShortUrlException);
        }
    }

    String generateShortUrl(String longUrl) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_TOKEN);

        String requestBody = String.format("{\"long_url\": \"%s\"}", longUrl);
        System.out.println("Request body: " + requestBody);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        if(response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("link").asText();
            } catch (JsonProcessingException e) {
                System.out.println("Error processing JSON response:" + e.getMessage());
                throw new RuntimeException("Error processing JSON response", e);
            }
        } else {
            System.out.println("Error creating short URL: " + response.getStatusCode());
            throw new RuntimeException("Error creating short url");
        }
    }

    @Override
    public UrlResponse getLongUrl(String shortUrl){
        System.out.println("Retrieving long URL: " + shortUrl);
        if (shortUrl == null || shortUrl.isEmpty()){
            throw new IllegalArgumentException("Url cannot be empty");
        }
        UrlEntity urlEntity = urlRepo.findByShortUrl(shortUrl);
        if (urlEntity == null) {
            throw new IllegalArgumentException("Url does not exist");
        }
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.setLongUrl(urlEntity.getLongUrl());
        urlResponse.setShortUrl(urlEntity.getShortUrl());
        return urlResponse;
    }
}
