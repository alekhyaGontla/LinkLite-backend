
package com.linklite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.linklite.dto.UrlRequest;
import com.linklite.dto.UrlResponse;
import com.linklite.entity.Url;
import com.linklite.redis.RedisService;
import com.linklite.repository.UrlRepository;
import com.linklite.service.ActivityService;
import com.linklite.serviceImpl.UrlServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private UrlServiceImpl urlService;

    private Url url;

    @BeforeEach
    void setup() {

        ReflectionTestUtils.setField(
                urlService,
                "baseUrl",
                "http://localhost:8080"
        );

        url = new Url();

        url.setId(1L);
        url.setOriginalUrl("https://www.google.com");
        url.setShortCode("google");
        url.setCustomAlias("google");
        url.setClickCount(0L);
        url.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateShortUrl() {

        UrlRequest request = new UrlRequest();

        request.setOriginalUrl("https://www.google.com");
        request.setCustomAlias("google");

        when(urlRepository.existsByCustomAlias("google"))
                .thenReturn(false);

        when(urlRepository.save(any(Url.class)))
                .thenReturn(url);

        doNothing()
                .when(activityService)
                .saveActivity(anyString(), anyString());

        doNothing()
                .when(redisService)
                .saveUrl(anyString(), anyString());

        UrlResponse response =
                urlService.createShortUrl(request);

        assertNotNull(response);

        assertEquals(
                "google",
                response.getShortCode()
        );

        assertEquals(
                "https://www.google.com",
                response.getOriginalUrl()
        );

        verify(urlRepository, times(1))
                .save(any(Url.class));
    }

    @Test
    void testGetOriginalUrl() {

        String shortCode = "google";

        when(redisService.getUrl(shortCode))
                .thenReturn(null);

        when(urlRepository.findByShortCode(shortCode))
                .thenReturn(Optional.of(url));

        when(urlRepository.save(any(Url.class)))
                .thenReturn(url);

        doNothing()
                .when(activityService)
                .saveActivity(anyString(), anyString());

        doNothing()
                .when(redisService)
                .saveUrl(anyString(), anyString());

        String original =
                urlService.getOriginalUrl(shortCode);

        assertEquals(
                "https://www.google.com",
                original
        );

        verify(urlRepository)
                .findByShortCode(shortCode);
    }

    @Test
    void testDeleteUrl() {

        Long id = 1L;

        when(urlRepository.findById(id))
                .thenReturn(Optional.of(url));

        doNothing()
                .when(redisService)
                .deleteUrl(anyString());

        doNothing()
                .when(urlRepository)
                .delete(any(Url.class));

        doNothing()
                .when(activityService)
                .saveActivity(anyString(), anyString());

        urlService.deleteUrl(id);

        verify(redisService)
                .deleteUrl(eq("google"));

        verify(urlRepository)
                .delete(url);

        verify(activityService)
                .saveActivity(
                        eq("DELETE"),
                        anyString()
                );
    }

    @Test
    void testAliasAlreadyExists() {

        UrlRequest request = new UrlRequest();

        request.setOriginalUrl("https://www.google.com");
        request.setCustomAlias("google");

        when(urlRepository.existsByCustomAlias("google"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> urlService.createShortUrl(request)
        );

        assertEquals(
                "Alias already exists",
                exception.getMessage()
        );
    }

    @Test
    void testUrlNotFound() {

        when(redisService.getUrl("xyz"))
                .thenReturn(null);

        when(urlRepository.findByShortCode("xyz"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> urlService.getOriginalUrl("xyz")
        );
    }
}
