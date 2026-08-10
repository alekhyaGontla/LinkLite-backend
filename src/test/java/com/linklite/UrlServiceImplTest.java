<<<<<<< HEAD

package com.linklite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

=======
package com.linklite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
<<<<<<< HEAD

=======
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

<<<<<<< HEAD
import org.springframework.test.util.ReflectionTestUtils;

=======
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
import com.linklite.dto.UrlRequest;
import com.linklite.dto.UrlResponse;
import com.linklite.entity.Url;
import com.linklite.redis.RedisService;
import com.linklite.repository.UrlRepository;
<<<<<<< HEAD
import com.linklite.service.ActivityService;
import com.linklite.serviceImpl.UrlServiceImpl;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {
=======
import com.linklite.serviceImpl.UrlServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UrlServiceImplTest {
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private RedisService redisService;

<<<<<<< HEAD
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
=======
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
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
    }

    @Test
    void testCreateShortUrl() {

        UrlRequest request = new UrlRequest();
<<<<<<< HEAD
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
=======

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
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
    }

    @Test
    void testGetOriginalUrl() {

<<<<<<< HEAD
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
=======
        when(redisService.getUrl("google"))
                .thenReturn(null);

        when(urlRepository.findByShortCode("google"))
                .thenReturn(Optional.of(url));

        String original = urlService.getOriginalUrl("google");

        assertEquals("https://www.google.com", original);
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
    }

    @Test
    void testDeleteUrl() {

<<<<<<< HEAD
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
=======
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
    
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
}