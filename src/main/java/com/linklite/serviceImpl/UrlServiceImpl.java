package com.linklite.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.linklite.dto.UrlRequest;
import com.linklite.dto.UrlResponse;
import com.linklite.entity.Url;
import com.linklite.exception.DuplicateAliasException;
import com.linklite.exception.UrlExpiredException;
import com.linklite.exception.UrlNotFoundException;
import com.linklite.redis.RedisService;
import com.linklite.repository.UrlRepository;
import com.linklite.service.ActivityService;
import com.linklite.service.UrlService;
import com.linklite.util.HashGenerator;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final RedisService redisService;
    private final ActivityService activityService;

    @Value("${app.base-url}")
    private String baseUrl;

    public UrlServiceImpl(
            UrlRepository urlRepository,
            RedisService redisService,
            ActivityService activityService) {

        this.urlRepository = urlRepository;
        this.redisService = redisService;
        this.activityService = activityService;
    }

    @Override
    public UrlResponse createShortUrl(UrlRequest request) {

        Url url = new Url();

        url.setOriginalUrl(request.getOriginalUrl());

        // Custom Alias
        if (request.getCustomAlias() != null
                && !request.getCustomAlias().isBlank()) {

            String alias = request.getCustomAlias();

            // Check against BOTH custom aliases and auto-generated short
            // codes - shortCode has a unique DB constraint, so a collision
            // there would previously blow up as a raw 500 instead of this
            // friendly error.
            if (urlRepository.existsByCustomAlias(alias)
                    || urlRepository.existsByShortCode(alias)) {
                throw new DuplicateAliasException("Alias already exists");
            }

            url.setShortCode(alias);
            url.setCustomAlias(alias);

        } else {

            String code;

            do {
                code = HashGenerator.generateShortCode();
            } while (urlRepository.existsByShortCode(code));

            url.setShortCode(code);
        }

        url.setClickCount(0L);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiryDate(request.getExpiryDate());

        Url saved = urlRepository.save(url);

        // Save activity
        activityService.saveActivity(
                "CREATE",
                "Created short URL : " + saved.getShortCode()
        );

        // Save URL in Redis
        redisService.saveUrl(
                saved.getShortCode(),
                saved.getOriginalUrl()
        );

        return convertToResponse(saved);
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        // Check Redis first
        String cachedUrl = redisService.getUrl(shortCode);

        if (cachedUrl != null) {

            Url url = urlRepository.findByShortCode(shortCode)
                    .orElseThrow(() ->
                            new UrlNotFoundException("URL Not Found")
                    );

            // Check expiry even when URL comes from Redis
            if (url.getExpiryDate() != null
                    && url.getExpiryDate().isBefore(LocalDateTime.now())) {

                redisService.deleteUrl(shortCode);

                throw new UrlExpiredException("URL Expired");
            }

            updateAnalytics(url);

            return cachedUrl;
        }

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("URL Not Found")
                );

        // Expiry check
        if (url.getExpiryDate() != null
                && url.getExpiryDate().isBefore(LocalDateTime.now())) {

            throw new UrlExpiredException("URL Expired");
        }

        updateAnalytics(url);

        // Save to Redis
        redisService.saveUrl(
                shortCode,
                url.getOriginalUrl()
        );

        return url.getOriginalUrl();
    }

    private void updateAnalytics(Url url) {

        url.setClickCount(
                url.getClickCount() + 1
        );

        url.setLastAccessed(
                LocalDateTime.now()
        );

        urlRepository.save(url);

        // Activity log
        activityService.saveActivity(
                "CLICK",
                "Opened short URL : " + url.getShortCode()
        );
    }

    @Override
    public List<UrlResponse> getAllUrls() {

        return urlRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public UrlResponse getAnalytics(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("URL Not Found")
                );

        activityService.saveActivity(
                "ANALYTICS",
                "Viewed analytics for : " + shortCode
        );

        return convertToResponse(url);
    }

    private UrlResponse convertToResponse(Url url) {

        UrlResponse response = new UrlResponse();

        response.setId(url.getId());

        response.setOriginalUrl(
                url.getOriginalUrl()
        );

        response.setShortCode(
                url.getShortCode()
        );

        response.setShortUrl(
                baseUrl + "/api/urls/" + url.getShortCode()
        );

        response.setCustomAlias(
                url.getCustomAlias()
        );

        response.setClickCount(
                url.getClickCount()
        );

        response.setCreatedAt(
                url.getCreatedAt()
        );

        response.setLastAccessed(
                url.getLastAccessed()
        );

        response.setExpiryDate(
                url.getExpiryDate()
        );

        return response;
    }

    @Override
    public void deleteUrl(Long id) {

        Url url = urlRepository.findById(id)
                .orElseThrow(() ->
                        new UrlNotFoundException("URL Not Found")
                );

        // Remove Redis cache
        redisService.deleteUrl(
                url.getShortCode()
        );

        // Delete database
        urlRepository.delete(url);

        // Activity log
        activityService.saveActivity(
                "DELETE",
                "Deleted short URL : " + url.getShortCode()
        );
    }
}
