package org.URLShortener.controller;

import org.URLShortener.dtos.UrlRequest;
import org.URLShortener.dtos.UrlResponse;
import org.URLShortener.service.URLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlController {
    @Autowired
    private URLService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> createShortUrl(@RequestBody UrlRequest urlRequest) {
       try{
           UrlResponse response = urlService.createShortUrl(urlRequest);
           return new ResponseEntity<>(response, HttpStatus.CREATED);
       } catch (Exception exception) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlResponse> getLongUrl(@PathVariable("shortUrl") String shortUrl) {
        try{
            UrlResponse response = urlService.getLongUrl(shortUrl);
            if (response == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
