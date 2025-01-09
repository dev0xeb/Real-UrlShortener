package org.URLShortener.service;

import org.URLShortener.dtos.UrlRequest;
import org.URLShortener.dtos.UrlResponse;

public interface URLService {
    UrlResponse createShortUrl(UrlRequest urlRequest);
    UrlResponse getLongUrl(String shortUrl);
}
