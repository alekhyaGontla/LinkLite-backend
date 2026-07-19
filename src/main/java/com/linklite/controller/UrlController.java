package com.linklite.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.linklite.dto.UrlRequest;
import com.linklite.dto.UrlResponse;
import com.linklite.service.UrlService;

@RestController
@RequestMapping("/api/urls")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://linklite.vercel.app"
        }
)
public class UrlController {


    private final UrlService urlService;


    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }


    // Create Short URL
    @PostMapping
    public ResponseEntity<UrlResponse> createShortUrl(
            @Valid @RequestBody UrlRequest request) {

        UrlResponse response =
                urlService.createShortUrl(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }



    // Redirect Short URL
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {


        String originalUrl =
                urlService.getOriginalUrl(shortCode);


        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", originalUrl)
                .build();
    }



    // Get All URLs
    @GetMapping
    public ResponseEntity<List<UrlResponse>> getAllUrls() {


        return ResponseEntity.ok(
                urlService.getAllUrls()
        );
    }



    // Delete URL
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUrl(
            @PathVariable Long id) {


        urlService.deleteUrl(id);


        return ResponseEntity.ok(
                "URL deleted successfully."
        );
    }



    // Analytics
    @GetMapping("/analytics/{shortCode}")
    public ResponseEntity<UrlResponse> getAnalytics(
            @PathVariable String shortCode) {


        return ResponseEntity.ok(
                urlService.getAnalytics(shortCode)
        );
    }

}