package com.linklite.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.linklite.dto.UrlRequest;
import com.linklite.dto.UrlResponse;
import com.linklite.entity.Url;
import com.linklite.redis.RedisService;
import com.linklite.repository.UrlRepository;
import com.linklite.service.UrlService;
import com.linklite.util.HashGenerator;

@Service
public class UrlServiceImpl implements UrlService {


    @Value("${app.base-url}")
    private String baseUrl;


    @Autowired
    private UrlRepository urlRepository;


    @Autowired
    private RedisService redisService;



    @Override
    public UrlResponse createShortUrl(UrlRequest request) {

        Url url = new Url();

        url.setOriginalUrl(request.getOriginalUrl());


        // Custom Alias
        if (request.getCustomAlias() != null &&
                !request.getCustomAlias().isBlank()) {


            if (urlRepository.existsByCustomAlias(request.getCustomAlias())) {
                throw new RuntimeException("Alias already exists");
            }


            url.setShortCode(request.getCustomAlias());
            url.setCustomAlias(request.getCustomAlias());


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
                            new RuntimeException("URL Not Found")
                    );


            updateAnalytics(url);


            return cachedUrl;
        }



        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new RuntimeException("URL Not Found")
                );



        // Check expiry

        if (url.getExpiryDate() != null &&
                url.getExpiryDate().isBefore(LocalDateTime.now())) {

            throw new RuntimeException("URL Expired");
        }



        updateAnalytics(url);



        // Save in Redis

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
                        new RuntimeException("URL Not Found")
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
                        new RuntimeException("URL Not Found")
                );



        // Remove Redis cache

        redisService.deleteUrl(
                url.getShortCode()
        );



        // Delete database record

        urlRepository.delete(url);

    }

}