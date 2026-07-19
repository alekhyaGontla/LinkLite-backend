package com.linklite.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UrlRequest {

    @NotBlank(message = "Original URL is required")
    @Pattern(
        regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
        message = "Please enter a valid URL"
    )
    private String originalUrl;

    private String customAlias;

    private LocalDateTime expiryDate;
}