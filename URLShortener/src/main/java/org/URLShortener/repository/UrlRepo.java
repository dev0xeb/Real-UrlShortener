package org.URLShortener.repository;

import org.URLShortener.data.UrlEntity;
import org.URLShortener.dtos.UrlResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepo extends MongoRepository<UrlEntity, String> {
    UrlResponse findByLongUrl(String longUrl);

    UrlEntity findByShortUrl(String shortUrl);
}
