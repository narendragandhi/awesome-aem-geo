package com.awesomeaem.geo.models.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.awesomeaem.geo.models.SeoMetadataModel;

/**
 * Unit Tests for SEO Metadata Model Implementation
 */
@DisplayName("SEO Metadata Model - Unit Tests")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SeoMetadataModelImplTest {

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private Resource resource;

    @Mock
    private ValueMap valueMap;

    @Mock
    private Resource pageResource;

    @Mock
    private Resource pageContentResource;

    private SeoMetadataModelImpl model;

    @BeforeEach
    void setUp() throws Exception {
        model = new SeoMetadataModelImpl();
        
        // Inject mocks via reflection
        Field requestField = SeoMetadataModelImpl.class.getDeclaredField("request");
        requestField.setAccessible(true);
        requestField.set(model, request);

        // Default mock behavior
        when(request.getResource()).thenReturn(resource);
        when(resource.getParent()).thenReturn(pageContentResource);
        when(pageContentResource.getParent()).thenReturn(pageResource);
        when(pageResource.getValueMap()).thenReturn(valueMap);
        when(pageResource.getChild("jcr:content")).thenReturn(pageContentResource);
        when(pageContentResource.getValueMap()).thenReturn(valueMap);
        when(valueMap.get("jcr:primaryType", String.class)).thenReturn("cq:Page");
    }

    @Nested
    @DisplayName("Title Tests")
    class TitleTests {

        @Test
        @DisplayName("Title under 60 chars should not be truncated")
        void title_under_limit_should_not_truncate() throws Exception {
            // Given
            String pageTitle = "AEM SEO Best Practices";
            when(valueMap.get("jcr:title", String.class)).thenReturn(pageTitle);

            // When
            String title = model.getTitle();

            // Then
            assertEquals(pageTitle, title);
            assertTrue(title.length() < SeoMetadataModel.TITLE_MAX_LENGTH);
        }

        @Test
        @DisplayName("Title over 60 chars should be truncated")
        void title_over_60_chars_should_truncate() throws Exception {
            // Given
            String longTitle = "This is a very long title that definitely exceeds the maximum character limit of sixty characters for SEO";
            when(valueMap.get("jcr:title", String.class)).thenReturn(longTitle);

            // When
            String title = model.getTitle();

            // Then
            assertEquals(SeoMetadataModel.TITLE_MAX_LENGTH, title.length());
            assertTrue(title.endsWith("..."));
        }

        @Test
        @DisplayName("SEO title should take precedence over page title")
        void seo_title_should_take_precedence() throws Exception {
            // Given
            String seoTitle = "Custom SEO Title";
            String pageTitle = "Page Title";
            when(valueMap.get("seoTitle", String.class)).thenReturn(seoTitle);
            when(valueMap.get("jcr:title", String.class)).thenReturn(pageTitle);

            // When
            String title = model.getTitle();

            // Then
            assertEquals(seoTitle, title);
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Description under 160 chars should not be truncated")
        void description_under_limit_should_not_truncate() throws Exception {
            // Given
            String description = "Learn about AEM SEO best practices for better search rankings.";
            when(valueMap.get("jcr:description", String.class)).thenReturn(description);

            // When
            String result = model.getDescription();

            // Then
            assertEquals(description, result);
            assertTrue(result.length() < SeoMetadataModel.DESC_MAX_LENGTH);
        }

        @Test
        @DisplayName("Description over 160 chars should be truncated")
        void description_over_160_chars_should_truncate() throws Exception {
            // Given
            String longDesc = "This is a very long meta description that definitely exceeds the maximum character limit of one hundred sixty characters which is recommended by search engines for optimal display in search results pages.";
            when(valueMap.get("jcr:description", String.class)).thenReturn(longDesc);

            // When
            String result = model.getDescription();

            // Then
            assertEquals(SeoMetadataModel.DESC_MAX_LENGTH, result.length());
            assertTrue(result.endsWith("..."));
        }
    }

    @Nested
    @DisplayName("Canonical URL Tests")
    class CanonicalUrlTests {

        @Test
        @DisplayName("Should generate canonical from page path")
        void should_generate_canonical_from_path() throws Exception {
            // Given
            String pagePath = "/content/mysite/en/products/widget";
            when(valueMap.get("canonicalUrl", String.class)).thenReturn(null);
            when(valueMap.get("siteDomain", String.class)).thenReturn("https://www.example.com");
            when(pageResource.getPath()).thenReturn(pagePath);

            // When
            String canonical = model.getCanonicalUrl();

            // Then
            assertNotNull(canonical);
            assertTrue(canonical.startsWith("https://"));
            assertTrue(canonical.endsWith(".html"));
        }

        @Test
        @DisplayName("Explicit canonical should be used when set")
        void explicit_canonical_should_be_used() throws Exception {
            // Given
            String explicitCanonical = "https://www.example.com/preferred-url";
            when(valueMap.get("canonicalUrl", String.class)).thenReturn(explicitCanonical);

            // When
            String canonical = model.getCanonicalUrl();

            // Then
            assertEquals(explicitCanonical, canonical);
        }
    }

    @Nested
    @DisplayName("OpenGraph Tests")
    class OpenGraphTests {

        @Test
        @DisplayName("OG title should fall back to page title")
        void ogTitle_should_fallback_to_title() throws Exception {
            // Given
            String pageTitle = "Page Title";
            when(valueMap.get("ogTitle", String.class)).thenReturn(null);
            when(valueMap.get("jcr:title", String.class)).thenReturn(pageTitle);

            // When
            String ogTitle = model.getOgTitle();

            // Then
            assertEquals(pageTitle, ogTitle);
        }

        @Test
        @DisplayName("OG description should fall back to meta description")
        void ogDescription_should_fallback_to_description() throws Exception {
            // Given
            String description = "Meta Description";
            when(valueMap.get("ogDescription", String.class)).thenReturn(null);
            when(valueMap.get("jcr:description", String.class)).thenReturn(description);

            // When
            String ogDesc = model.getOgDescription();

            // Then
            assertEquals(description, ogDesc);
        }

        @Test
        @DisplayName("OG image should use default when not set")
        void ogImage_should_use_default() throws Exception {
            // Given
            when(valueMap.get("ogImage", String.class)).thenReturn(null);

            // When
            String ogImage = model.getOgImage();

            // Then
            assertEquals(SeoMetadataModel.DEFAULT_OG_IMAGE, ogImage);
        }

        @Test
        @DisplayName("OG type should default to website")
        void ogType_should_default_to_website() throws Exception {
            // Given
            when(valueMap.get("ogType", String.class)).thenReturn(null);

            // When
            String ogType = model.getOgType();

            // Then
            assertEquals("website", ogType);
        }
    }

    @Nested
    @DisplayName("Twitter Card Tests")
    class TwitterCardTests {

        @Test
        @DisplayName("Twitter card should default to summary_large_image")
        void twitterCard_should_default_to_summary_large_image() throws Exception {
            // Given
            when(valueMap.get("twitterCard", String.class)).thenReturn(null);

            // When
            String twitterCard = model.getTwitterCard();

            // Then
            assertEquals("summary_large_image", twitterCard);
        }
    }

    @Nested
    @DisplayName("Robots Meta Tests")
    class RobotsMetaTests {

        @Test
        @DisplayName("Default robots should be index,follow")
        void default_robots_should_be_index_follow() throws Exception {
            // Given - default values are null
            // When
            String robots = model.getRobots();

            // Then
            assertEquals("index,follow", robots);
        }

        @Test
        @DisplayName("Noindex should prevent indexing")
        void noindex_should_prevent_indexing() throws Exception {
            // Given
            when(valueMap.get("noIndex", Boolean.class)).thenReturn(true);

            // When
            String robots = model.getRobots();

            // Then
            assertEquals("noindex,follow", robots);
        }

        @Test
        @DisplayName("Nofollow should prevent link following")
        void nofollow_should_prevent_link_following() throws Exception {
            // Given
            when(valueMap.get("noFollow", Boolean.class)).thenReturn(true);

            // When
            String robots = model.getRobots();

            // Then
            assertEquals("index,nofollow", robots);
        }

        @Test
        @DisplayName("Noindex and nofollow combined")
        void combined_noindex_nofollow() throws Exception {
            // Given
            when(valueMap.get("noIndex", Boolean.class)).thenReturn(true);
            when(valueMap.get("noFollow", Boolean.class)).thenReturn(true);

            // When
            String robots = model.getRobots();

            // Then
            assertEquals("noindex,nofollow", robots);
        }

        @Test
        @DisplayName("isIndexable returns true by default")
        void isIndexable_returns_true_by_default() throws Exception {
            // When
            boolean indexable = model.isIndexable();

            // Then
            assertTrue(indexable);
        }

        @Test
        @DisplayName("isFollowable returns true by default")
        void isFollowable_returns_true_by_default() throws Exception {
            // When
            boolean followable = model.isFollowable();

            // Then
            assertTrue(followable);
        }
    }

    @Nested
    @DisplayName("Locale Tests")
    class LocaleTests {

        @Test
        @DisplayName("Locale should be extracted from path")
        void locale_should_be_extracted_from_path() throws Exception {
            // Given
            String pagePath = "/content/mysite/en-us/products/widget";
            when(resource.getPath()).thenReturn(pagePath);

            // When
            String locale = model.getLocale();

            // Then
            assertEquals("en_US", locale);
        }

        @Test
        @DisplayName("Locale with underscore should work")
        void locale_with_underscore_should_work() throws Exception {
            // Given
            String pagePath = "/content/mysite/en_US/about";
            when(resource.getPath()).thenReturn(pagePath);

            // When
            String locale = model.getLocale();

            // Then
            assertEquals("en_US", locale);
        }
    }
}
