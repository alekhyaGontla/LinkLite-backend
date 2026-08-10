<<<<<<< HEAD

=======
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
package com.linklite.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
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

<<<<<<< HEAD
    private final UrlRepository urlRepository;
    private final RedisService redisService;
    private final ActivityService activityService;
=======
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38

    @Value("${app.base-url}")
    private String baseUrl;

<<<<<<< HEAD
    public UrlServiceImpl(
            UrlRepository urlRepository,
            RedisService redisService,
            ActivityService activityService) {

        this.urlRepository = urlRepository;
        this.redisService = redisService;
        this.activityService = activityService;
    }
=======

    @Autowired
    private UrlRepository urlRepository;


    @Autowired
    private RedisService redisService;


    // Activity tracking
    @Autowired
    private ActivityService activityService;


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38

    @Override
    public UrlResponse createShortUrl(UrlRequest request) {

<<<<<<< HEAD
        Url url = new Url();

        url.setOriginalUrl(request.getOriginalUrl());

        // Custom Alias
        if (request.getCustomAlias() != null
                && !request.getCustomAlias().isBlank()) {

            if (urlRepository.existsByCustomAlias(request.getCustomAlias())) {
                throw new DuplicateAliasException("Alias already exists");
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

        // Activity tracking
=======

        Url url = new Url();


        url.setOriginalUrl(request.getOriginalUrl());



        // Custom Alias

        if (request.getCustomAlias() != null &&
                !request.getCustomAlias().isBlank()) {



            if (urlRepository.existsByCustomAlias(request.getCustomAlias())) {

                throw new DuplicateAliasException("Alias already exists");

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



        // Save activity

>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        activityService.saveActivity(
                "CREATE",
                "Created short URL : " + saved.getShortCode()
        );

<<<<<<< HEAD
        // Save URL in Redis
=======



        // Save URL in Redis

>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        redisService.saveUrl(
                saved.getShortCode(),
                saved.getOriginalUrl()
        );

<<<<<<< HEAD
        return convertToResponse(saved);
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        // Check Redis first
        String cachedUrl = redisService.getUrl(shortCode);

        if (cachedUrl != null) {

=======


        return convertToResponse(saved);

    }







    @Override
    public String getOriginalUrl(String shortCode) {



        // Check Redis first

        String cachedUrl = redisService.getUrl(shortCode);



        if (cachedUrl != null) {



>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
            Url url = urlRepository.findByShortCode(shortCode)
                    .orElseThrow(() ->
                            new UrlNotFoundException("URL Not Found")
                    );

<<<<<<< HEAD
            // Check expiry even when URL comes from Redis
            if (url.getExpiryDate() != null
                    && url.getExpiryDate().isBefore(LocalDateTime.now())) {

                redisService.deleteUrl(shortCode);

                throw new UrlExpiredException("URL Expired");
            }

            updateAnalytics(url);

            return cachedUrl;
        }

=======


            updateAnalytics(url);



            return cachedUrl;

        }






>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("URL Not Found")
                );

<<<<<<< HEAD
        // Expiry check
        if (url.getExpiryDate() != null
                && url.getExpiryDate().isBefore(LocalDateTime.now())) {

            throw new UrlExpiredException("URL Expired");
        }

        updateAnalytics(url);

        // Save to Redis
=======




        // Expiry check

        if (url.getExpiryDate() != null &&
                url.getExpiryDate().isBefore(LocalDateTime.now())) {


            throw new UrlExpiredException("URL Expired");

        }






        updateAnalytics(url);





        // Save to Redis

>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        redisService.saveUrl(
                shortCode,
                url.getOriginalUrl()
        );

<<<<<<< HEAD
        return url.getOriginalUrl();
    }

    private void updateAnalytics(Url url) {

=======


        return url.getOriginalUrl();

    }







    private void updateAnalytics(Url url) {



>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        url.setClickCount(
                url.getClickCount() + 1
        );

<<<<<<< HEAD
=======


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        url.setLastAccessed(
                LocalDateTime.now()
        );

<<<<<<< HEAD
        urlRepository.save(url);

        // Activity log
=======


        urlRepository.save(url);





        // Activity log

>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        activityService.saveActivity(
                "CLICK",
                "Opened short URL : " + url.getShortCode()
        );
<<<<<<< HEAD
    }

    @Override
    public List<UrlResponse> getAllUrls() {

=======


    }









    @Override
    public List<UrlResponse> getAllUrls() {



>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        return urlRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
<<<<<<< HEAD
    }

    @Override
    public UrlResponse getAnalytics(String shortCode) {

=======

    }









    @Override
    public UrlResponse getAnalytics(String shortCode) {



>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("URL Not Found")
                );

<<<<<<< HEAD
=======


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        activityService.saveActivity(
                "ANALYTICS",
                "Viewed analytics for : " + shortCode
        );

<<<<<<< HEAD
        return convertToResponse(url);
    }

    private UrlResponse convertToResponse(Url url) {

        UrlResponse response = new UrlResponse();

        response.setId(url.getId());

=======


        return convertToResponse(url);

    }









    private UrlResponse convertToResponse(Url url) {



        UrlResponse response = new UrlResponse();



        response.setId(url.getId());



>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        response.setOriginalUrl(
                url.getOriginalUrl()
        );

<<<<<<< HEAD
=======


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        response.setShortCode(
                url.getShortCode()
        );

<<<<<<< HEAD
=======


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        response.setShortUrl(
                baseUrl + "/api/urls/" + url.getShortCode()
        );

<<<<<<< HEAD
=======


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        response.setCustomAlias(
                url.getCustomAlias()
        );

<<<<<<< HEAD
=======


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        response.setClickCount(
                url.getClickCount()
        );

<<<<<<< HEAD
=======


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        response.setCreatedAt(
                url.getCreatedAt()
        );

<<<<<<< HEAD
=======


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        response.setLastAccessed(
                url.getLastAccessed()
        );

<<<<<<< HEAD
=======


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        response.setExpiryDate(
                url.getExpiryDate()
        );

<<<<<<< HEAD
        return response;
    }

    @Override
    public void deleteUrl(Long id) {

=======


        return response;

    }









    @Override
    public void deleteUrl(Long id) {



>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        Url url = urlRepository.findById(id)
                .orElseThrow(() ->
                        new UrlNotFoundException("URL Not Found")
                );

<<<<<<< HEAD
        // Remove Redis cache
=======




        // Remove Redis cache

>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        redisService.deleteUrl(
                url.getShortCode()
        );

<<<<<<< HEAD
        // Delete database
        urlRepository.delete(url);

        // Activity log
=======




        // Delete database

        urlRepository.delete(url);





        // Activity log

>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
        activityService.saveActivity(
                "DELETE",
                "Deleted short URL : " + url.getShortCode()
        );
<<<<<<< HEAD
    }
=======


    }


>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
}
