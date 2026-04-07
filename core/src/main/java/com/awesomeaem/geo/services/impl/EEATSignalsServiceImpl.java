package com.awesomeaem.geo.services.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

import com.awesomeaem.geo.services.EEATSignalsService;

/**
 * Implementation of EEATSignalsService.
 * Generates E-E-A-T structured data for SEO and LLM optimization.
 * 
 * @see EEATSignalsService
 */
@Component(service = EEATSignalsService.class)
public class EEATSignalsServiceImpl implements EEATSignalsService {

    @Override
    public Map<String, Object> generateAuthorSchema(AuthorInfo author) {
        if (author == null || StringUtils.isBlank(author.name())) {
            return null;
        }

        Map<String, Object> schema = new HashMap<>();
        schema.put("@type", "Person");
        schema.put("name", sanitizeHtml(author.name()));

        if (StringUtils.isNotBlank(author.url()) && isValidUrl(author.url())) {
            schema.put("url", author.url());
        }

        if (StringUtils.isNotBlank(author.jobTitle())) {
            schema.put("jobTitle", author.jobTitle());
        }

        if (author.credentials() != null && !author.credentials().isEmpty()) {
            List<String> validCredentials = author.credentials().stream()
                .filter(c -> StringUtils.isNotBlank(c))
                .collect(Collectors.toList());
            if (!validCredentials.isEmpty()) {
                schema.put("knowsAbout", validCredentials);
            }
        }

        if (StringUtils.isNotBlank(author.image()) && isValidUrl(author.image())) {
            schema.put("image", author.image());
        }

        return schema;
    }

    @Override
    public Map<String, Object> generateOrganizationSchema(OrganizationInfo organization) {
        if (organization == null || StringUtils.isBlank(organization.name())) {
            return null;
        }

        Map<String, Object> schema = new HashMap<>();
        schema.put("@type", "Organization");
        schema.put("name", organization.name());

        if (StringUtils.isNotBlank(organization.url()) && isValidUrl(organization.url())) {
            schema.put("url", organization.url());
        }

        if (StringUtils.isNotBlank(organization.logo()) && isValidUrl(organization.logo())) {
            schema.put("logo", organization.logo());
        }

        if (organization.certifications() != null && !organization.certifications().isEmpty()) {
            List<String> validCerts = organization.certifications().stream()
                .filter(c -> StringUtils.isNotBlank(c))
                .collect(Collectors.toList());
            if (!validCerts.isEmpty()) {
                schema.put("knowsAbout", validCerts);
            }
        }

        if (StringUtils.isNotBlank(organization.sameAs())) {
            List<String> sameAs = List.of(organization.sameAs());
            schema.put("sameAs", sameAs);
        }

        return schema;
    }

    @Override
    public Map<String, Object> generateReviewSchema(ReviewInfo review, String itemType) {
        if (review == null) {
            return null;
        }

        Map<String, Object> schema = new HashMap<>();
        schema.put("@type", StringUtils.isBlank(itemType) ? "Product" : itemType);

        Map<String, Object> aggregateRating = new HashMap<>();
        aggregateRating.put("@type", "AggregateRating");
        aggregateRating.put("ratingValue", review.ratingValue());
        aggregateRating.put("reviewCount", review.reviewCount());
        schema.put("aggregateRating", aggregateRating);

        if (StringUtils.isNotBlank(review.reviewUrl())) {
            schema.put("reviewUrl", review.reviewUrl());
        }

        return schema;
    }

    @Override
    public Map<String, Object> generateFactCheckSchema(String claim, String factCheckUrl, boolean isTrue) {
        if (StringUtils.isBlank(claim)) {
            return null;
        }

        Map<String, Object> schema = new HashMap<>();
        schema.put("@type", "ClaimReview");
        
        Map<String, Object> itemReviewed = new HashMap<>();
        itemReviewed.put("@type", "Claim");
        itemReviewed.put("text", claim);
        schema.put("itemReviewed", itemReviewed);

        Map<String, Object> reviewRating = new HashMap<>();
        reviewRating.put("@type", "Rating");
        reviewRating.put("ratingValue", isTrue ? "True" : "False");
        schema.put("reviewRating", reviewRating);

        if (StringUtils.isNotBlank(factCheckUrl)) {
            schema.put("url", factCheckUrl);
        }

        return schema;
    }

    @Override
    public Map<String, Object> generateTrustBadgeSchema(TrustBadge badge) {
        if (badge == null) {
            return null;
        }

        Map<String, Object> schema = new HashMap<>();
        
        if (StringUtils.isNotBlank(badge.name())) {
            schema.put("name", badge.name());
        }
        
        if (StringUtils.isNotBlank(badge.imageUrl())) {
            schema.put("image", badge.imageUrl());
        }
        
        if (StringUtils.isNotBlank(badge.issuer())) {
            schema.put("issuer", badge.issuer());
        }

        return schema;
    }

    @Override
    public Map<String, Object> generateProvenanceData(Instant publishedDate, Instant lastModifiedDate, String editorialProcess) {
        Map<String, Object> provenance = new HashMap<>();

        if (publishedDate != null) {
            provenance.put("datePublished", publishedDate.toString());
        }

        if (lastModifiedDate != null) {
            provenance.put("dateModified", lastModifiedDate.toString());
        }

        if (StringUtils.isNotBlank(editorialProcess)) {
            provenance.put("editorialProcess", editorialProcess);
        }

        return provenance;
    }

    @Override
    public String generateCompleteEEATJsonLd(AuthorInfo author, OrganizationInfo organization, 
                                               ReviewInfo review, TrustBadge badge, 
                                               Map<String, Object> provenance) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"@context\":\"https://schema.org\"");
        
        if (author != null && StringUtils.isNotBlank(author.name())) {
            Map<String, Object> authorSchema = generateAuthorSchema(author);
            if (authorSchema != null) {
                json.append(",\"author\":").append(toJson(authorSchema));
            }
        }

        if (organization != null && StringUtils.isNotBlank(organization.name())) {
            Map<String, Object> orgSchema = generateOrganizationSchema(organization);
            if (orgSchema != null) {
                json.append(",\"publisher\":").append(toJson(orgSchema));
            }
        }

        if (review != null) {
            Map<String, Object> aggregateRating = buildAggregateRating(review);
            if (aggregateRating != null) {
                json.append(",\"aggregateRating\":").append(toJson(aggregateRating));
            }
        }

        json.append("}");
        return json.toString();
    }

    private String toJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        
        StringBuilder json = new StringBuilder("{");
        String entries = map.entrySet().stream()
            .map(e -> "\"" + e.getKey() + "\":" + formatValue(e.getValue()))
            .collect(Collectors.joining(","));
        json.append(entries).append("}");
        return json.toString();
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + escapeJson((String) value) + "\"";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (list.isEmpty()) {
                return "[]";
            }
            String items = list.stream()
                .map(this::formatValue)
                .collect(Collectors.joining(","));
            return "[" + items + "]";
        }
        return "\"" + escapeJson(value.toString()) + "\"";
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    private String sanitizeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("<[^>]*>", "");
    }

    private Map<String, Object> buildAggregateRating(ReviewInfo review) {
        if (review == null) {
            return null;
        }
        Map<String, Object> aggregateRating = new HashMap<>();
        aggregateRating.put("@type", "AggregateRating");
        aggregateRating.put("ratingValue", review.ratingValue());
        aggregateRating.put("reviewCount", review.reviewCount());
        return aggregateRating;
    }

    private boolean isValidUrl(String urlString) {
        if (StringUtils.isBlank(urlString)) {
            return false;
        }
        try {
            URI uri = new URI(urlString);
            return StringUtils.isNotBlank(uri.getScheme());
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
