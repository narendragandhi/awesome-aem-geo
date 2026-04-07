package com.awesomeaem.geo.models.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.awesomeaem.geo.models.EEATSignalsModel;
import com.awesomeaem.geo.services.EEATSignalsService;

/**
 * Implementation of EEATSignalsModel.
 * Exports E-E-A-T signals for SEO and LLM optimization.
 * 
 * @see EEATSignalsModel
 */
@Model(adaptables = {SlingHttpServletRequest.class, org.apache.sling.api.resource.Resource.class},
       resourceType = "awesome-aem-geo/components/geo/eeat-signals")
public class EEATSignalsModelImpl implements EEATSignalsModel {

    @OSGiService
    private EEATSignalsService eeatSignalsService;

    @ValueMapValue(name = "authorName")
    private String authorName;

    @ValueMapValue(name = "authorUrl")
    private String authorUrl;

    @ValueMapValue(name = "authorJobTitle")
    private String authorJobTitle;

    @ChildResource(name = "authorCredentials")
    private List<Resource> authorCredentials;

    @ValueMapValue(name = "authorImage")
    private String authorImage;

    @ValueMapValue(name = "organizationName")
    private String organizationName;

    @ValueMapValue(name = "organizationUrl")
    private String organizationUrl;

    @ValueMapValue(name = "organizationLogo")
    private String organizationLogo;

    @ChildResource(name = "certifications")
    private List<Resource> certifications;

    @ValueMapValue(name = "factCheckUrl")
    private String factCheckUrl;

    @ValueMapValue(name = "reviewRating")
    private Double reviewRating;

    @ValueMapValue(name = "reviewCount")
    private Integer reviewCount;

    @ChildResource(name = "trustBadges")
    private List<Resource> trustBadges;

    @ValueMapValue(name = "publishedDate")
    private Calendar publishedDate;

    @ValueMapValue(name = "lastModifiedDate")
    private Calendar lastModifiedDate;

    @ValueMapValue(name = "editorialProcess")
    private String editorialProcess;

    @Override
    public String getExportedType() {
        return "awesome-aem-geo/components/geo/eeat-signals";
    }

    @Override
    public String getAuthorName() {
        return authorName;
    }

    @Override
    public String getAuthorUrl() {
        return authorUrl;
    }

    @Override
    public String getAuthorJobTitle() {
        return authorJobTitle;
    }

    @Override
    public List<String> getAuthorCredentials() {
        return extractStringList(authorCredentials, "value");
    }

    @Override
    public String getAuthorImage() {
        return authorImage;
    }

    @Override
    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public String getOrganizationUrl() {
        return organizationUrl;
    }

    @Override
    public String getOrganizationLogo() {
        return organizationLogo;
    }

    @Override
    public List<String> getCertifications() {
        return extractStringList(certifications, "value");
    }

    @Override
    public String getFactCheckUrl() {
        return factCheckUrl;
    }

    @Override
    public Double getReviewRating() {
        return reviewRating;
    }

    @Override
    public Integer getReviewCount() {
        return reviewCount;
    }

    @Override
    public List<String> getTrustBadges() {
        return extractStringList(trustBadges, "value");
    }

    @Override
    public Instant getPublishedDate() {
        return toInstant(publishedDate);
    }

    @Override
    public Instant getLastModifiedDate() {
        return toInstant(lastModifiedDate);
    }

    @Override
    public String getEditorialProcess() {
        return editorialProcess;
    }

    @Override
    public String getEeatJsonLd() {
        if (eeatSignalsService == null) {
            return "";
        }

        EEATSignalsService.AuthorInfo author = null;
        if (StringUtils.isNotBlank(authorName)) {
            author = new EEATSignalsService.AuthorInfo(
                authorName,
                authorUrl,
                authorJobTitle,
                getAuthorCredentials(),
                authorImage
            );
        }

        EEATSignalsService.OrganizationInfo organization = null;
        if (StringUtils.isNotBlank(organizationName)) {
            organization = new EEATSignalsService.OrganizationInfo(
                organizationName,
                organizationUrl,
                organizationLogo,
                getCertifications(),
                null
            );
        }

        EEATSignalsService.ReviewInfo review = null;
        if (reviewRating != null && reviewCount != null) {
            review = new EEATSignalsService.ReviewInfo(
                reviewRating,
                reviewCount,
                factCheckUrl
            );
        }

        String json = eeatSignalsService.generateCompleteEEATJsonLd(
            author,
            organization,
            review,
            null,
            null
        );

        return StringUtils.defaultString(json);
    }

    private List<String> extractStringList(List<Resource> resources, String propertyName) {
        if (resources == null || resources.isEmpty()) {
            return List.of();
        }
        List<String> values = new ArrayList<>();
        for (Resource resource : resources) {
            if (resource == null) {
                continue;
            }
            String value = resource.getValueMap().get(propertyName, String.class);
            if (StringUtils.isNotBlank(value)) {
                values.add(value);
            }
        }
        return values;
    }

    private Instant toInstant(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return calendar.toInstant();
    }
}
