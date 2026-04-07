package com.awesomeaem.geo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.awesomeaem.geo.services.impl.ImageSeoServiceImpl;

@DisplayName("ImageSeoService Specification")
class ImageSeoSpecTest {

    private ImageSeoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ImageSeoServiceImpl();
    }

    @Nested
    @DisplayName("Generate Image Schema")
    class GenerateImageSchema {
        @Test
        @DisplayName("Should generate ImageObject schema for valid image")
        void shouldGenerateImageObjectSchema() {
            Resource imageResource = mock(Resource.class);
            Map<String, Object> map = new HashMap<>();
            map.put("jcr:title", "Test Image");
            map.put("jcr:description", "A test image description");
            map.put("fileReference", "/content/dam/test.jpg");
            map.put("alt", "Alt text for image");
            map.put("width", 800);
            map.put("height", 600);
            map.put("mimeType", "image/jpeg");
            ValueMap properties = new ValueMapDecorator(map);
            when(imageResource.getValueMap()).thenReturn(properties);

            String schema = service.generateImageSchema(imageResource);

            assertNotNull(schema);
            assertTrue(schema.contains("\"@type\":\"ImageObject\""));
            assertTrue(schema.contains("Test Image"));
            assertTrue(schema.contains("A test image description"));
            assertTrue(schema.contains("800"));
            assertTrue(schema.contains("600"));
            assertTrue(schema.contains("image/jpeg"));
        }

        @Test
        @DisplayName("Should handle image with minimal properties")
        void shouldHandleMinimalProperties() {
            Resource imageResource = mock(Resource.class);
            Map<String, Object> map = new HashMap<>();
            map.put("fileReference", "/content/dam/minimal.jpg");
            ValueMap properties = new ValueMapDecorator(map);
            when(imageResource.getValueMap()).thenReturn(properties);

            String schema = service.generateImageSchema(imageResource);

            assertNotNull(schema);
            assertTrue(schema.contains("\"@type\":\"ImageObject\""));
        }

        @Test
        @DisplayName("Should return empty string for null resource")
        void shouldReturnEmptyForNullResource() {
            String schema = service.generateImageSchema(null);
            assertEquals("", schema);
        }
    }

    @Nested
    @DisplayName("Alt Text Validation")
    class AltTextValidation {
        @Test
        @DisplayName("Should return true for image with alt text")
        void shouldReturnTrueForValidAltText() {
            Resource imageResource = mock(Resource.class);
            Map<String, Object> map = new HashMap<>();
            map.put("alt", "Valid alt text");
            ValueMap properties = new ValueMapDecorator(map);
            when(imageResource.getValueMap()).thenReturn(properties);

            boolean result = service.validateAltText(imageResource);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for missing alt text")
        void shouldReturnFalseForMissingAltText() {
            Resource imageResource = mock(Resource.class);
            Map<String, Object> map = new HashMap<>();
            map.put("fileReference", "/content/dam/test.jpg");
            ValueMap properties = new ValueMapDecorator(map);
            when(imageResource.getValueMap()).thenReturn(properties);

            boolean result = service.validateAltText(imageResource);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for empty alt text")
        void shouldReturnFalseForEmptyAltText() {
            Resource imageResource = mock(Resource.class);
            Map<String, Object> map = new HashMap<>();
            map.put("alt", "   ");
            ValueMap properties = new ValueMapDecorator(map);
            when(imageResource.getValueMap()).thenReturn(properties);

            boolean result = service.validateAltText(imageResource);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for null resource")
        void shouldReturnFalseForNullResource() {
            boolean result = service.validateAltText(null);
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Extract Image Metadata")
    class ExtractMetadata {
        @Test
        @DisplayName("Should extract all metadata properties")
        void shouldExtractAllMetadata() {
            Resource imageResource = mock(Resource.class);
            Map<String, Object> map = new HashMap<>();
            map.put("jcr:title", "My Image");
            map.put("jcr:description", "Image description");
            map.put("alt", "Alt text");
            map.put("width", 1024);
            map.put("height", 768);
            map.put("caption", "A caption");
            map.put("tags", new String[]{"tag1", "tag2"});
            ValueMap properties = new ValueMapDecorator(map);
            when(imageResource.getValueMap()).thenReturn(properties);

            Map<String, Object> metadata = service.extractImageMetadata(imageResource);

            assertNotNull(metadata);
            assertEquals("My Image", metadata.get("title"));
            assertEquals("Image description", metadata.get("description"));
            assertEquals("Alt text", metadata.get("altText"));
            assertEquals(1024, metadata.get("width"));
            assertEquals(768, metadata.get("height"));
            assertEquals("A caption", metadata.get("caption"));
        }

        @Test
        @DisplayName("Should handle null resource gracefully")
        void shouldHandleNullResource() {
            Map<String, Object> metadata = service.extractImageMetadata(null);
            assertNotNull(metadata);
            assertTrue(metadata.isEmpty());
        }
    }

    @Nested
    @DisplayName("Image Sitemap Entries")
    class ImageSitemapEntries {
        @Test
        @DisplayName("Should return empty list when resolver is unavailable")
        void shouldReturnEmptyWhenResolverUnavailable() {
            List<String> entries = service.getImageSitemapEntries("/content/dam");

            assertNotNull(entries);
            assertTrue(entries.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list for null path")
        void shouldReturnEmptyForNullPath() {
            List<String> entries = service.getImageSitemapEntries(null);
            assertNotNull(entries);
            assertTrue(entries.isEmpty());
        }
    }
}
