package com.linklite.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UrlResponse {

    private Long id;
    private String originalUrl;
    private String shortCode;
    private String shortUrl;
    private String customAlias;
    private Long clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessed;
    private LocalDateTime expiryDate;

}