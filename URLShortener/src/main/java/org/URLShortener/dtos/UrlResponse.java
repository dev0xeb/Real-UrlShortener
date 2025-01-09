package org.URLShortener.dtos;

import lombok.Data;

@Data
public class UrlResponse {
    private String longUrl;
    private String shortUrl;
}
