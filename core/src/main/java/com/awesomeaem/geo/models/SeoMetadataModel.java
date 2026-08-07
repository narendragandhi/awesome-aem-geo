package com.awesomeaem.geo.models;

import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;

/**
 * SEO Metadata Model Interface
 * 
 * Defines the API for SEO metadata properties.
 * This interface is used by Sling Models to export JSON.
 * 
 * @see SeoMetadataModelImpl
 */
@Model(
    adaptables = org.apache.sling.api.SlingHttpServletRequest.class,
    adapters = {SeoMetadataModel.class, ComponentExporter.class},
    resourceType = SeoMetadataModel.RESOURCE_TYPE
)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public interface SeoMetadataModel extends ComponentExporter {

    /**
     * Resource type for this component
     */
    String RESOURCE_TYPE = "awesome-aem-geo/components/seo/seo-metadata";

    /**
     * Maximum title length in characters
     */
    int TITLE_MAX_LENGTH = 60;

    /**
     * Maximum description length in characters
     */
    int DESC_MAX_LENGTH = 160;

    /**
     * Default OG image path
     */
    String DEFAULT_OG_IMAGE = "/content/dam/awesome-aem-geo/default-og.png";

    /**
     * Default site domain
     */
    String DEFAULT_DOMAIN = "https://www.example.com";

    /**
     * Get the page title
     * 
     * @return truncated title (max 60 chars) or fallback
     */
    String getTitle();

    /**
     * Get the meta description
     * 
     * @return truncated description (max 160 chars) or fallback
     */
    String getDescription();

    /**
     * Get the canonical URL
     * 
     * @return absolute canonical URL
     */
    String getCanonicalUrl();

    /**
     * Get OpenGraph title
     * 
     * @return OG title (falls back to title if not set)
     */
    String getOgTitle();

    /**
     * Get OpenGraph description
     * 
     * @return OG description (falls back to description if not set)
     */
    String getOgDescription();

    /**
     * Get OpenGraph image URL
     * 
     * @return OG image URL (uses default if not set)
     */
    String getOgImage();

    /**
     * Get OpenGraph type
     * 
     * @return OG type (defaults to "website")
     */
    String getOgType();

    /**
     * Get Twitter Card type
     * 
     * @return Twitter card type (defaults to "summary_large_image")
     */
    String getTwitterCard();

    /**
     * Get Twitter title
     * 
     * @return Twitter title (falls back to ogTitle)
     */
    String getTwitterTitle();

    /**
     * Get Twitter description
     * 
     * @return Twitter description (falls back to ogDescription)
     */
    String getTwitterDescription();

    /**
     * Get Twitter image
     * 
     * @return Twitter image (falls back to ogImage)
     */
    String getTwitterImage();

    /**
     * Get robots meta directive
     * 
     * @return robots directive (e.g., "index,follow")
     */
    String getRobots();

    /**
     * Whether search and AI result previews should be suppressed.
     *
     * @return true when the page emits the nosnippet directive
     */
    boolean isNoSnippet();

    /**
     * Maximum preview length, or -1 when no limit is configured.
     *
     * @return max-snippet value in words
     */
    int getMaxSnippet();

    /**
     * Get locale
     * 
     * @return locale string (e.g., "en_US")
     */
    String getLocale();

    /**
     * Check if this page should be indexed
     * 
     * @return true if page should be indexed
     */
    boolean isIndexable();

    /**
     * Check if links should be followed
     * 
     * @return true if links should be followed
     */
    boolean isFollowable();
}
