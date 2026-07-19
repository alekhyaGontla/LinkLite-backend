package com.linklite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.linklite.dto.UrlRequest;
import com.linklite.dto.UrlResponse;
import com.linklite.entity.Url;
import com.linklite.redis.RedisService;
import com.linklite.repository.UrlRepository;
import com.linklite.serviceImpl.UrlServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private UrlServiceImpl urlService;

    private Url url;

    @BeforeEach
    void setup() {

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

        UrlResponse response = urlService.createShortUrl(request);

        assertNotNull(response);
        assertEquals("google", response.getShortCode());
        assertEquals("https://www.google.com", response.getOriginalUrl());

        verify(urlRepository, times(1)).save(any(Url.class));
    }

    @Test
    void testGetOriginalUrl() {

        when(redisService.getUrl("google"))
                .thenReturn(null);

        when(urlRepository.findByShortCode("google"))
                .thenReturn(Optional.of(url));

        String original = urlService.getOriginalUrl("google");

        assertEquals("https://www.google.com", original);
    }

    @Test
    void testDeleteUrl() {

        when(urlRepository.findById(1L))
                .thenReturn(Optional.of(url));

        urlService.deleteUrl(1L);

        verify(urlRepository).delete(url);
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
                () -> urlService.createShortUrl(request));

        assertEquals("Alias already exists", exception.getMessage());
    }
    
    @Test
    void testUrlNotFound() {

        when(redisService.getUrl("xyz"))
                .thenReturn(null);

        when(urlRepository.findByShortCode("xyz"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> urlService.getOriginalUrl("xyz"));
    }
    
}