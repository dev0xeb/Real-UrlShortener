package org.URLShortener.service;

import org.URLShortener.data.UrlEntity;
import org.URLShortener.dtos.UrlRequest;
import org.URLShortener.dtos.UrlResponse;
import org.URLShortener.repository.UrlRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UrlServiceImplTest {
    @Autowired
    private UrlServiceImpl urlService;

    @Autowired
    private UrlRepo urlRepo;

    @BeforeEach
    public void setUp() {
        urlRepo.deleteAll();
    }

    @Test
    public void testCreateShortUrl() {
        UrlRequest urlRequest = new UrlRequest();
        urlRequest.setLongUrl("https://hireclinton.com");

        UrlResponse urlResponse = urlService.createShortUrl(urlRequest);
        assertNotNull(urlResponse);
        assertNotNull(urlResponse.getShortUrl());
        assertEquals("https://hireclinton.com", urlResponse.getLongUrl());
    }

    @Test
    public void testCreateShortUrlWithForeignCharacters() {
        UrlRequest urlRequest = new UrlRequest();
        urlRequest.setLongUrl("https://hireclinton.com/こんにちは");

        UrlResponse urlResponse = urlService.createShortUrl(urlRequest);
        assertNotNull(urlResponse);
        assertNotNull(urlResponse.getShortUrl());
        assertEquals("https://hireclinton.com/こんにちは", urlResponse.getLongUrl());
    }

    @Test
    public void testCreateShortUrlForExistingLongUrl(){
        UrlRequest urlRequest = new UrlRequest();
        urlRequest.setLongUrl("https://hireclinton.com");

        UrlEntity existingUrl = new UrlEntity();
        existingUrl.setLongUrl("https://hireclinton.com");
        existingUrl.setShortUrl("https://bit.ly/existing");
        urlRepo.save(existingUrl);
        UrlResponse urlResponse = urlService.createShortUrl(urlRequest);
        assertNotNull(urlResponse);
        assertEquals("https://hireclinton.com", urlResponse.getLongUrl());
        assertEquals("https://bit.ly/existing", urlResponse.getShortUrl());
    }

    @Test
    public void testGetLongUrl(){
        UrlEntity savedUrl = new UrlEntity();
        savedUrl.setLongUrl("https://hireclinton.com");
        savedUrl.setShortUrl("https://bit.ly/existing");
        urlRepo.save(savedUrl);

        UrlResponse urlResponse = urlService.getLongUrl("https://bit.ly/existing");
        assertNotNull(urlResponse);
        assertEquals("https://hireclinton.com", urlResponse.getLongUrl());
    }

    @Test
    public void testCreateShortUrlWithInvalidLongUrl(){
        UrlRequest urlRequest = new UrlRequest();
        urlRequest.setLongUrl("invalid url");

        Exception invalidUrlException = assertThrows(IllegalArgumentException.class, () -> {
            urlService.createShortUrl(urlRequest);
        });
        String expectedMessage = "Invalid Long Url";
        String actualMessage = invalidUrlException.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testCreateShortUrlWithValidLongUrl(){
        UrlRequest urlRequest = new UrlRequest();
        urlRequest.setLongUrl("https://hireclinton.com");
        UrlResponse urlResponse = urlService.createShortUrl(urlRequest);
        assertNotNull(urlResponse);
        assertNotNull(urlResponse.getShortUrl());
        assertEquals("https://hireclinton.com", urlResponse.getLongUrl());

        UrlResponse savedUrl = new UrlResponse();
        savedUrl = urlRepo.findByLongUrl("https://hireclinton.com");
        assertNotNull(savedUrl);
        assertEquals("https://hireclinton.com", savedUrl.getLongUrl());
        assertEquals(urlResponse.getShortUrl(), savedUrl.getShortUrl());
    }

}