package com.linklite.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UrlRequest {

    @NotBlank(message = "Original URL is required")
    @Pattern(
        regexp = "^https?://.+",
        message = "Original URL must start with http:// or https://"
    )
    private String originalUrl;

    // Optional - only validated when the user actually provides one
    @Pattern(
        regexp = "^[a-zA-Z0-9_-]{3,30}$",
        message = "Custom alias must be 3-30 characters: letters, numbers, hyphens or underscores only"
    )
    private String customAlias;

    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expiryDate;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
