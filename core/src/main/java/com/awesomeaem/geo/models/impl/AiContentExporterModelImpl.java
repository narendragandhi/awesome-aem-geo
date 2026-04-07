package com.awesomeaem.geo.models.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.cq.export.json.ComponentExporter;
import com.awesomeaem.geo.models.AiContentExporterModel;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of AiContentExporterModel.
 * Exports content optimized for AI/LLM consumption.
 * 
 * @see AiContentExporterModel
 */
@Slf4j
@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    adapters = {AiContentExporterModel.class, ComponentExporter.class},
    resourceType = "awesome-aem-geo/components/structure/ai-content-exporter"
)
public class AiContentExporterModelImpl implements AiContentExporterModel {

    private static final String EXPORTED_TYPE = "awesome-aem-geo/components/structure/ai-content-exporter";

    @Self
    private SlingHttpServletRequest request;

    @RequestAttribute
    @Default(values = "")
    private String title;

    @RequestAttribute
    @Default(values = "")
    private String description;

    @RequestAttribute
    @Default(values = "")
    private String url;

    @RequestAttribute
    private Instant publishedDate;

    @RequestAttribute
    private Instant modifiedDate;

    @RequestAttribute
    @Default(values = "")
    private String authorName;

    @RequestAttribute
    @Default(values = "")
    private String authorUrl;

    @Getter
    private List<Heading> headings = new ArrayList<>();

    @Getter
    private List<String> paragraphs = new ArrayList<>();

    @Getter
    private List<ImageInfo> images = new ArrayList<>();

    @Getter
    private List<LinkInfo> links = new ArrayList<>();

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getUrl() {
        if (StringUtils.isNotBlank(url)) {
            return url;
        }
        if (request != null) {
            return request.getRequestURL().toString();
        }
        return "";
    }

    @Override
    public Instant getPublishedDate() {
        return publishedDate;
    }

    @Override
    public Instant getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public com.awesomeaem.geo.models.AiContentExporterModel.AuthorInfo getAuthor() {
        if (StringUtils.isNotBlank(authorName)) {
            return new com.awesomeaem.geo.models.AiContentExporterModel.AuthorInfo(
                authorName,
                authorUrl != null ? authorUrl : ""
            );
        }
        return null;
    }

    @Override
    public Map<String, Object> getSchema() {
        Map<String, Object> schema = new HashMap<>();
        schema.put("@context", "https://schema.org");
        
        if (StringUtils.isNotBlank(title)) {
            schema.put("@type", "Article");
            schema.put("headline", title);
        }
        
        if (StringUtils.isNotBlank(description)) {
            schema.put("description", description);
        }
        
        if (publishedDate != null) {
            schema.put("datePublished", publishedDate.toString());
        }
        
        if (modifiedDate != null) {
            schema.put("dateModified", modifiedDate.toString());
        }
        
        if (StringUtils.isNotBlank(authorName)) {
            Map<String, Object> author = new HashMap<>();
            author.put("@type", "Person");
            author.put("name", authorName);
            if (StringUtils.isNotBlank(authorUrl)) {
                author.put("url", authorUrl);
            }
            schema.put("author", author);
        }
        
        if (StringUtils.isNotBlank(url)) {
            schema.put("url", url);
        }
        
        return schema;
    }

    @Override
    public String getExportedType() {
        return EXPORTED_TYPE;
    }
}
