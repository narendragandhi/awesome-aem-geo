package com.awesomeaem.geo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.awesomeaem.geo.services.impl.SitemapGeneratorServiceImpl;

@DisplayName("SitemapGeneratorService Coverage Tests")
class SitemapGeneratorCoverageTest {

    private SitemapGeneratorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SitemapGeneratorServiceImpl();
    }

    @Nested
    @DisplayName("Generate Sitemap")
    class GenerateSitemap {
        @Test
        @DisplayName("Should generate sitemap with default path")
        void shouldGenerateSitemapWithDefaultPath() {
            String sitemap = service.generateSitemap(null, 0);
            assertNotNull(sitemap);
            assertTrue(sitemap.contains("<urlset"));
        }

        @Test
        @DisplayName("Should generate sitemap with custom path")
        void shouldGenerateSitemapWithCustomPath() {
            String sitemap = service.generateSitemap("/content/site", 100);
            assertNotNull(sitemap);
            assertTrue(sitemap.contains("<urlset"));
        }

        @Test
        @DisplayName("Should handle missing resolver without url entries")
        void shouldHandleMissingResolver() {
            String sitemap = service.generateSitemap("/content/site", 100);
            assertFalse(sitemap.contains("<loc>"));
        }
    }

    @Nested
    @DisplayName("Generate Sitemap Index")
    class SitemapIndex {
        @Test
        @DisplayName("Should generate sitemap index")
        void shouldGenerateSitemapIndex() {
            List<String> urls = List.of(
                "https://example.com/sitemap1.xml",
                "https://example.com/sitemap2.xml"
            );
            String index = service.generateSitemapIndex(urls);
            assertNotNull(index);
            assertTrue(index.contains("<sitemapindex"));
            assertTrue(index.contains("<sitemap>"));
        }

        @Test
        @DisplayName("Should return empty for null input")
        void shouldReturnEmptyForNull() {
            String index = service.generateSitemapIndex(null);
            assertEquals("", index);
        }

        @Test
        @DisplayName("Should return empty for empty list")
        void shouldReturnEmptyForEmptyList() {
            String index = service.generateSitemapIndex(List.of());
            assertEquals("", index);
        }
    }

    @Nested
    @DisplayName("Should Include Page")
    class ShouldIncludePage {
        @Test
        @DisplayName("Should include normal page")
        void shouldIncludeNormalPage() {
            Map<String, Object> props = new HashMap<>();
            assertTrue(service.shouldIncludePage("/content/site/about", props));
        }

        @Test
        @DisplayName("Should exclude page with noindex")
        void shouldExcludeNoIndex() {
            Map<String, Object> props = new HashMap<>();
            props.put("noIndex", true);
            assertFalse(service.shouldIncludePage("/content/site/about", props));
        }

        @Test
        @DisplayName("Should exclude jcr paths")
        void shouldExcludeJcrPaths() {
            assertFalse(service.shouldIncludePage("/content/site/jcr:content", new HashMap<>()));
        }

        @Test
        @DisplayName("Should exclude libs paths")
        void shouldExcludeLibsPaths() {
            assertFalse(service.shouldIncludePage("/libs/cq", new HashMap<>()));
        }

        @Test
        @DisplayName("Should exclude bin paths")
        void shouldExcludeBinPaths() {
            assertFalse(service.shouldIncludePage("/bin/cq", new HashMap<>()));
        }

        @Test
        @DisplayName("Should exclude system paths")
        void shouldExcludeSystemPaths() {
            assertFalse(service.shouldIncludePage("/system/console", new HashMap<>()));
        }

        @Test
        @DisplayName("Should return false for blank path")
        void shouldReturnFalseForBlankPath() {
            assertFalse(service.shouldIncludePage("", new HashMap<>()));
            assertFalse(service.shouldIncludePage("   ", new HashMap<>()));
        }

        @Test
        @DisplayName("Should handle null properties")
        void shouldHandleNullProperties() {
            assertTrue(service.shouldIncludePage("/content/site/about", null));
        }
    }

    @Nested
    @DisplayName("Change Frequency")
    class ChangeFrequency {
        @Test
        @DisplayName("Should return default for null")
        void shouldReturnDefaultForNull() {
            assertEquals("weekly", service.getChangeFrequency(null));
        }

        @Test
        @DisplayName("Should return default for blank")
        void shouldReturnDefaultForBlank() {
            assertEquals("weekly", service.getChangeFrequency(""));
        }

        @Test
        @DisplayName("Should return weekly for news")
        void shouldReturnWeeklyForNews() {
            assertEquals("weekly", service.getChangeFrequency("/content/site/news"));
        }

        @Test
        @DisplayName("Should return weekly for blog")
        void shouldReturnWeeklyForBlog() {
            assertEquals("weekly", service.getChangeFrequency("/content/site/blog"));
        }

        @Test
        @DisplayName("Should return weekly for products")
        void shouldReturnWeeklyForProducts() {
            assertEquals("weekly", service.getChangeFrequency("/content/site/products"));
        }

        @Test
        @DisplayName("Should return weekly for about")
        void shouldReturnWeeklyForAbout() {
            assertEquals("weekly", service.getChangeFrequency("/content/site/about"));
        }

        @Test
        @DisplayName("Should return weekly for company")
        void shouldReturnWeeklyForCompany() {
            assertEquals("weekly", service.getChangeFrequency("/content/site/company"));
        }
    }

    @Nested
    @DisplayName("Priority")
    class Priority {
        @Test
        @DisplayName("Should return default for null")
        void shouldReturnDefaultForNull() {
            assertEquals(0.5, service.getPriority(null));
        }

        @Test
        @DisplayName("Should return default for blank")
        void shouldReturnDefaultForBlank() {
            assertEquals(0.5, service.getPriority(""));
        }

        @Test
        @DisplayName("Should return 1.0 for home page")
        void shouldReturnOneForHomePage() {
            assertEquals(1.0, service.getPriority("/content/site/en"));
            assertEquals(1.0, service.getPriority("/content/site/en.html"));
        }

        @Test
        @DisplayName("Should return 0.8 for level 1")
        void shouldReturnPointEightForLevel1() {
            assertEquals(0.8, service.getPriority("/content/site/en/about"));
        }

        @Test
        @DisplayName("Should return 0.6 for level 2")
        void shouldReturnPointSixForLevel2() {
            assertEquals(0.6, service.getPriority("/content/site/en/products/category"));
        }

        @Test
        @DisplayName("Should return 0.3 for level 3 and deeper")
        void shouldReturnPointThreeForLevel3() {
            assertEquals(0.3, service.getPriority("/content/site/en/products/category/item/deep"));
        }

        @Test
        @DisplayName("Should return 0.3 for deeper pages")
        void shouldReturnPointThreeForDeeper() {
            assertEquals(0.3, service.getPriority("/content/site/en/a/b/c/d/e/f"));
        }
    }
}
