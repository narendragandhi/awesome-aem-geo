package com.awesomeaem.geo.models;

import java.time.Instant;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;

/**
 * Sling Model for E-E-A-T (Experience, Expertise, Authoritativeness, Trustworthiness) signals.
 * 
 * <p>Exports structured provenance and trust data for the publishing contract.</p>
 * 
 * @since 1.0.0
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
@Exporter(name = "jackson", extensions = "json")
public interface EEATSignalsModel {

    /**
     * Get exported type for JSON exporter.
     * @return Exported type string
     */
    String getExportedType();

    // Author properties
    
    /** @return Author name */
    String getAuthorName();
    
    /** @return Author profile URL */
    String getAuthorUrl();
    
    /** @return Author job title */
    String getAuthorJobTitle();
    
    /** @return List of credentials */
    List<String> getAuthorCredentials();
    
    /** @return Author image URL */
    String getAuthorImage();

    // Organization properties
    
    /** @return Organization name */
    String getOrganizationName();
    
    /** @return Organization URL */
    String getOrganizationUrl();
    
    /** @return Organization logo URL */
    String getOrganizationLogo();
    
    /** @return List of certifications */
    List<String> getCertifications();

    // Trust signals
    
    /** @return Fact-check URL */
    String getFactCheckUrl();
    
    /** @return Review rating value */
    Double getReviewRating();
    
    /** @return Number of reviews */
    Integer getReviewCount();
    
    /** @return List of trust badges */
    List<String> getTrustBadges();

    // Provenance
    
    /** @return Publication date */
    Instant getPublishedDate();
    
    /** @return Last modified date */
    Instant getLastModifiedDate();
    
    /** @return Editorial process description */
    String getEditorialProcess();

    /**
     * Get combined E-E-A-T JSON-LD markup.
     * @return JSON-LD string or empty
     */
    String getEeatJsonLd();
}
