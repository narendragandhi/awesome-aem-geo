package com.awesomeaem.geo.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Service for generating E-E-A-T (Experience, Expertise, Authoritativeness, Trustworthiness) signals.
 * 
 * <p>E-E-A-T is a critical ranking factor for Google and LLMs. This service provides
 * structured data and metadata to demonstrate content credibility.</p>
 * 
 * @since 1.0.0
 */
public interface EEATSignalsService {

    /**
     * Author information for E-E-A-T signals.
     * 
     * @param name       Author's name
     * @param url        Author's profile URL
     * @param jobTitle   Author's job title
     * @param credentials List of credentials/degrees
     * @param image image      URL to author's
     */
    record AuthorInfo(
        String name,
        String url,
        String jobTitle,
        List<String> credentials,
        String image
    ) {}

    /**
     * Organization information for E-E-A-T signals.
     * 
     * @param name           Organization name
     * @param url            Organization website URL
     * @param logo           URL to organization logo
     * @param certifications List of certifications
     * @param sameAs         Social profile URLs
     */
    record OrganizationInfo(
        String name,
        String url,
        String logo,
        List<String> certifications,
        String sameAs
    ) {}

    /**
     * Review/rating information.
     * 
     * @param ratingValue  Rating value (e.g., 4.5)
     * @param reviewCount  Number of reviews
     * @param reviewUrl    URL to reviews page
     */
    record ReviewInfo(
        double ratingValue,
        int reviewCount,
        String reviewUrl
    ) {}

    /**
     * Trust badge information.
     * 
     * @param name       Badge name
     * @param imageUrl   Badge image URL
     * @param issuer     Badge issuer
     * @param issueDate  Date badge was issued
     */
    record TrustBadge(
        String name,
        String imageUrl,
        String issuer,
        String issueDate
    ) {}

    /**
     * Generate Person/Author schema.
     * @param author Author information
     * @return Schema.org JSON structure
     */
    Map<String, Object> generateAuthorSchema(AuthorInfo author);

    /**
     * Generate Organization schema.
     * @param organization Organization information
     * @return Schema.org JSON structure
     */
    Map<String, Object> generateOrganizationSchema(OrganizationInfo organization);

    /**
     * Generate Review/AggregateRating schema.
     * @param review   Review information
     * @param itemType Type of item being reviewed
     * @return Schema.org JSON structure
     */
    Map<String, Object> generateReviewSchema(ReviewInfo review, String itemType);

    /**
     * Generate ClaimReview schema for fact-checking.
     * @param claim        Claim being fact-checked
     * @param factCheckUrl URL to fact-check
     * @param isTrue       Whether claim is true
     * @return Schema.org JSON structure
     */
    Map<String, Object> generateFactCheckSchema(String claim, String factCheckUrl, boolean isTrue);

    /**
     * Generate TrustBadge schema.
     * @param badge Badge information
     * @return Schema.org JSON structure
     */
    Map<String, Object> generateTrustBadgeSchema(TrustBadge badge);

    /**
     * Generate content provenance data.
     * @param publishedDate    Publication date
     * @param lastModifiedDate Last modified date
     * @param editorialProcess Editorial process description
     * @return Provenance data map
     */
    Map<String, Object> generateProvenanceData(Instant publishedDate, Instant lastModifiedDate, String editorialProcess);

    /**
     * Generate complete E-E-A-T JSON-LD combining all signals.
     * @param author      Author information
     * @param organization Organization information
     * @param review      Review information
     * @param badge      Trust badge
     * @param provenance Provenance data
     * @return JSON-LD string
     */
    String generateCompleteEEATJsonLd(AuthorInfo author, OrganizationInfo organization, ReviewInfo review, TrustBadge badge, Map<String, Object> provenance);
}
