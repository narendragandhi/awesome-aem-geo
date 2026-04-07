package com.awesomeaem.geo.models;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ComponentExporter;
import com.awesomeaem.geo.services.AiBotHandlerService;

/**
 * Sling Model for exporting content optimized for AI/LLM consumption.
 * 
 * <p>Provides structured content export that LLMs can easily parse and understand.</p>
 * 
 * @since 1.0.0
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
@Exporter(name = "jackson", extensions = "json")
public interface AiContentExporterModel extends ComponentExporter {

    /**
     * Get page title.
     * @return Title
     */
    String getTitle();

    /**
     * Get page description.
     * @return Description
     */
    String getDescription();

    /**
     * Get page URL.
     * @return URL
     */
    String getUrl();

    /**
     * Get publication date.
     * @return Published date
     */
    Instant getPublishedDate();

    /**
     * Get last modified date.
     * @return Modified date
     */
    Instant getModifiedDate();

    /**
     * Get author information.
     * @return Author visit info
     */
    AuthorInfo getAuthor();

    /**
     * Get all headings in content.
     * @return List of headings
     */
    List<Heading> getHeadings();

    /**
     * Get all paragraphs.
     * @return List of paragraph texts
     */
    List<String> getParagraphs();

    /**
     * Get all images.
     * @return List of image info
     */
    List<ImageInfo> getImages();

    /**
     * Get all links.
     * @return List of link info
     */
    List<LinkInfo> getLinks();

    /**
     * Get schema.org data.
     * @return Schema data map
     */
    Map<String, Object> getSchema();

    /**
     * Represents a heading.
     * @param level Heading level (1-6)
     * @param text  Heading text
     */
    record Heading(int level, String text) {}

    /**
     * Represents an author.
     * @param name Author name
     * @param url  Author URL
     */
    record AuthorInfo(String name, String url) {}

    /**
     * Represents an image.
     * @param url     Image URL
     * @param alt     Alt text
     * @param caption Caption
     */
    record ImageInfo(String url, String alt, String caption) {}

    /**
     * Represents a link.
     * @param text Link text
     * @param url  Link URL
     */
    record LinkInfo(String text, String url) {}
}
