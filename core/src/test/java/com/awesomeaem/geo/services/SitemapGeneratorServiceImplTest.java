package com.awesomeaem.geo.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.awesomeaem.geo.services.impl.SitemapGeneratorServiceImpl;

@DisplayName("SitemapGeneratorServiceImpl - Unit Tests")
class SitemapGeneratorServiceImplTest {

    @Test
    @DisplayName("Should return empty urlset when resolver unavailable")
    void shouldReturnEmptyUrlsetWhenResolverUnavailable() {
        SitemapGeneratorServiceImpl service = new SitemapGeneratorServiceImpl();
        String xml = service.generateSitemap("/content", 10);

        assertNotNull(xml);
        assertTrue(xml.contains("<urlset"));
    }
}
