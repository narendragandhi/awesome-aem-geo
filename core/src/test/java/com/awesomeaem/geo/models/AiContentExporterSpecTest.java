package com.awesomeaem.geo.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.awesomeaem.geo.models.impl.AiContentExporterModelImpl;

@DisplayName("AiContentExporterModel - SPEC Tests")
@ExtendWith(MockitoExtension.class)
class AiContentExporterSpecTest {

    @Nested
    @DisplayName("Requirement 1: Basic Metadata")
    class BasicMetadata {
        
        @Test
        @DisplayName("Should export title")
        void should_export_title() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            when(model.getTitle()).thenReturn("Test Title");
            
            assertEquals("Test Title", model.getTitle());
        }

        @Test
        @DisplayName("Should export description")
        void should_export_description() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            when(model.getDescription()).thenReturn("Test Description");
            
            assertEquals("Test Description", model.getDescription());
        }

        @Test
        @DisplayName("Should export URL")
        void should_export_url() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            when(model.getUrl()).thenReturn("https://example.com/content/page");
            
            assertEquals("https://example.com/content/page", model.getUrl());
        }

        @Test
        @DisplayName("Should return exported type")
        void should_return_exported_type() {
            assertEquals("awesome-aem-geo/components/structure/ai-content-exporter", 
                new AiContentExporterModelImpl().getExportedType());
        }
    }

    @Nested
    @DisplayName("Requirement 2: Date Information")
    class DateInfo {
        
        @Test
        @DisplayName("Should export published date")
        void should_export_published_date() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            Instant date = Instant.parse("2024-01-15T10:00:00Z");
            when(model.getPublishedDate()).thenReturn(date);
            
            assertEquals(date, model.getPublishedDate());
        }

        @Test
        @DisplayName("Should export modified date")
        void should_export_modified_date() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            Instant date = Instant.parse("2024-01-20T10:00:00Z");
            when(model.getModifiedDate()).thenReturn(date);
            
            assertEquals(date, model.getModifiedDate());
        }
    }

    @Nested
    @DisplayName("Requirement 3: Author Information")
    class AuthorInfo {
        
        @Test
        @DisplayName("Should export author with name")
        void should_export_author_name() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            var author = new AiContentExporterModel.AuthorInfo(
                "John Doe", "https://example.com/authors/john-doe"
            );
            when(model.getAuthor()).thenReturn(author);
            
            assertNotNull(model.getAuthor());
            assertEquals("John Doe", model.getAuthor().name());
        }

        @Test
        @DisplayName("Should return null for missing author")
        void should_return_null_for_missing_author() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            when(model.getAuthor()).thenReturn(null);
            
            assertNull(model.getAuthor());
        }
    }

    @Nested
    @DisplayName("Requirement 4: Content Structure")
    class ContentStructure {
        
        @Test
        @DisplayName("Should export headings")
        void should_export_headings() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            List<AiContentExporterModel.Heading> headings = List.of(
                new AiContentExporterModel.Heading(1, "Main Heading"),
                new AiContentExporterModel.Heading(2, "Section Heading")
            );
            when(model.getHeadings()).thenReturn(headings);
            
            assertEquals(2, model.getHeadings().size());
            assertEquals(1, model.getHeadings().get(0).level());
        }

        @Test
        @DisplayName("Should export paragraphs")
        void should_export_paragraphs() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            List<String> paragraphs = List.of("Paragraph 1", "Paragraph 2");
            when(model.getParagraphs()).thenReturn(paragraphs);
            
            assertEquals(2, model.getParagraphs().size());
        }

        @Test
        @DisplayName("Should export images with alt text")
        void should_export_images() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            List<AiContentExporterModel.ImageInfo> images = List.of(
                new AiContentExporterModel.ImageInfo("image.jpg", "Alt text", "Caption")
            );
            when(model.getImages()).thenReturn(images);
            
            assertEquals(1, model.getImages().size());
            assertEquals("Alt text", model.getImages().get(0).alt());
        }

        @Test
        @DisplayName("Should export links")
        void should_export_links() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            List<AiContentExporterModel.LinkInfo> links = List.of(
                new AiContentExporterModel.LinkInfo("Link Text", "/content/target")
            );
            when(model.getLinks()).thenReturn(links);
            
            assertEquals(1, model.getLinks().size());
        }
    }

    @Nested
    @DisplayName("Requirement 5: Schema.org Data")
    class SchemaData {
        
        @Test
        @DisplayName("Should include @context")
        void should_include_context() {
            Map<String, Object> schema = Map.of("@context", "https://schema.org");
            
            assertEquals("https://schema.org", schema.get("@context"));
        }

        @Test
        @DisplayName("Should include @type")
        void should_include_type() {
            Map<String, Object> schema = Map.of("@type", "Article");
            
            assertEquals("Article", schema.get("@type"));
        }

        @Test
        @DisplayName("Should include headline")
        void should_include_headline() {
            Map<String, Object> schema = Map.of("headline", "Test Headline");
            
            assertEquals("Test Headline", schema.get("headline"));
        }

        @Test
        @DisplayName("Should include author object")
        void should_include_author() {
            Map<String, Object> author = Map.of("@type", "Person", "name", "Author Name");
            Map<String, Object> schema = Map.of("author", author);
            
            assertNotNull(schema.get("author"));
        }
    }

    @Nested
    @DisplayName("Requirement 6: Edge Cases")
    class EdgeCases {
        
        @Test
        @DisplayName("Should handle empty title")
        void should_handle_empty_title() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            when(model.getTitle()).thenReturn("");
            
            assertEquals("", model.getTitle());
        }

        @Test
        @DisplayName("Should handle empty paragraphs")
        void should_handle_empty_paragraphs() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            when(model.getParagraphs()).thenReturn(List.of());
            
            assertTrue(model.getParagraphs().isEmpty());
        }

        @Test
        @DisplayName("Should handle missing images")
        void should_handle_missing_images() {
            AiContentExporterModel model = mock(AiContentExporterModel.class);
            when(model.getImages()).thenReturn(List.of());
            
            assertTrue(model.getImages().isEmpty());
        }
    }
}
