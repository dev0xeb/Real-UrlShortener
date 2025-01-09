package org.URLShortener.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document (collection = "urls")
public class UrlEntity {
    @Id
    private String id;
    private String longUrl;
    private String shortUrl;
}
