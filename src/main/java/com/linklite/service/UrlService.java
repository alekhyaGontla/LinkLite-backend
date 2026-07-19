package com.linklite.service;

import java.util.List;

import com.linklite.dto.UrlRequest;
import com.linklite.dto.UrlResponse;

public interface UrlService {
	 // Create a new short URL
    UrlResponse createShortUrl(UrlRequest request);

    // Get original URL using short code
    String getOriginalUrl(String shortCode);

    // Get all URLs
    List<UrlResponse> getAllUrls();

    // Delete URL
    void deleteUrl(Long id);
    
    UrlResponse getAnalytics(String shortCode);
}
