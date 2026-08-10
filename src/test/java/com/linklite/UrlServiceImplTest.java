
package com.linklite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private UrlServiceImpl urlService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                urlService,
                "baseUrl",
                "http://localhost:8080"
        );
    }

    @Test
    void testCreateShortUrl() {

        UrlRequest request = new UrlRequest();
        request.setOriginalUrl("https://google.com");

        Url savedUrl = new Url();
        savedUrl.setId(1L);
        savedUrl.setOriginalUrl("https://google.com");
        savedUrl.setShortCode("abc123");
        savedUrl.setClickCount(0L);

        when(urlRepository.existsByShortCode(anyString()))
                .thenReturn(false);

        when(urlRepository.save(any(Url.class)))
                .thenReturn(savedUrl);

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
                "abc123",
                response.getShortCode()
        );

        assertEquals(
                "https://google.com",
                response.getOriginalUrl()
        );

        verify(activityService)
                .saveActivity(
                        eq("CREATE"),
                        anyString()
                );
    }

    @Test
    void testGetOriginalUrl() {

        String shortCode = "abc123";

        Url url = new Url();

        url.setId(1L);
        url.setOriginalUrl("https://google.com");
        url.setShortCode(shortCode);
        url.setClickCount(0L);

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

        String result =
                urlService.getOriginalUrl(shortCode);

        assertEquals(
                "https://google.com",
                result
        );

        verify(activityService)
                .saveActivity(
                        eq("CLICK"),
                        anyString()
                );
    }

    @Test
    void testDeleteUrl() {

        Long id = 1L;

        Url url = new Url();

        url.setId(id);
        url.setShortCode("abc123");
        url.setOriginalUrl("https://google.com");

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
                .deleteUrl(eq("abc123"));

        verify(urlRepository)
                .delete(url);

        verify(activityService)
                .saveActivity(
                        eq("DELETE"),
                        anyString()
                );
    }
}