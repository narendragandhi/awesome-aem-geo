package com.awesomeaem.geo.models.impl;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Component;

import com.day.cq.commons.Externalizer;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.awesomeaem.geo.models.SeoMetadataModel;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of SEO Metadata Model
 * 
 * Provides SEO-critical metadata for AEM pages including:
 * - Title tag (with character limit)
 * - Meta description (with character limit)
 * - Canonical URL
 * - OpenGraph tags
 * - Twitter Card tags
 * - Robots meta directives
 * - Locale
 * 
 * Supports inheritance from parent pages.
 */
@Slf4j
@Component(service = SeoMetadataModel.class)
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {SeoMetadataModel.class},
    resourceType = SeoMetadataModel.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SeoMetadataModelImpl implements SeoMetadataModel {

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private Externalizer externalizer;

    private static final Pattern LOCALE_PATTERN = Pattern.compile("/content/[^/]+/([a-z]{2}(-[A-Z]{2})?)/.*");

    @Override
    public String getTitle() {
        String title = getInheritedPageProperty("seoTitle", String.class);
        if (StringUtils.isBlank(title)) {
            title = getInheritedPageProperty("jcr:title", String.class);
        }
        
        return truncateToLength(title, TITLE_MAX_LENGTH);
    }

    @Override
    public String getDescription() {
        String description = getInheritedPageProperty("seoDescription", String.class);
        if (StringUtils.isBlank(description)) {
            description = getInheritedPageProperty("jcr:description", String.class);
        }
        
        return truncateToLength(description, DESC_MAX_LENGTH);
    }

    @Override
    public String getCanonicalUrl() {
        String canonical = getInheritedPageProperty("canonicalUrl", String.class);

        if (StringUtils.isNotBlank(canonical)) {
            return toAbsoluteUrl(canonical);
        }
        
        return generateCanonicalFromPath();
    }

    @Override
    public String getOgTitle() {
        String inherited = getInheritedPageProperty("ogTitle", String.class);
        if (StringUtils.isNotBlank(inherited)) {
            return inherited;
        }
        return getTitle();
    }

    @Override
    public String getOgDescription() {
        String inherited = getInheritedPageProperty("ogDescription", String.class);
        if (StringUtils.isNotBlank(inherited)) {
            return inherited;
        }
        return getDescription();
    }

    @Override
    public String getOgImage() {
        String inherited = getInheritedPageProperty("ogImage", String.class);
        if (StringUtils.isNotBlank(inherited)) {
            return toAbsoluteUrl(inherited);
        }
        return DEFAULT_OG_IMAGE;
    }

    @Override
    public String getOgType() {
        String inherited = getInheritedPageProperty("ogType", String.class);
        if (StringUtils.isNotBlank(inherited)) {
            return inherited;
        }
        return "website";
    }

    @Override
    public String getTwitterCard() {
        String inherited = getInheritedPageProperty("twitterCard", String.class);
        if (StringUtils.isNotBlank(inherited)) {
            return inherited;
        }
        return "summary_large_image";
    }

    @Override
    public String getTwitterTitle() {
        return getOgTitle();
    }

    @Override
    public String getTwitterDescription() {
        return getOgDescription();
    }

    @Override
    public String getTwitterImage() {
        return getOgImage();
    }

    @Override
    public String getRobots() {
        List<String> directives = Arrays.asList(
            isIndexable() ? "index" : "noindex",
            isFollowable() ? "follow" : "nofollow"
        );
        return String.join(",", directives);
    }

    @Override
    public boolean isIndexable() {
        Boolean noIdx = getInheritedPageProperty("noIndex", Boolean.class);
        return noIdx == null || !noIdx;
    }

    @Override
    public boolean isFollowable() {
        Boolean noFllw = getInheritedPageProperty("noFollow", Boolean.class);
        return noFllw == null || !noFllw;
    }

    @Override
    public String getLocale() {
        String path = getPagePath();
        if (path != null) {
            Matcher matcher = LOCALE_PATTERN.matcher(path);
            if (matcher.find()) {
                String locale = matcher.group(1);
                return locale.replace("-", "_");
            }
        }
        return "en_US";
    }

    // Helper methods

    private <T> T getPageProperty(String name, Class<T> type) {
        Resource pageContent = getPageContentResource();
        if (pageContent == null) {
            return null;
        }
        ValueMap props = pageContent.getValueMap();
        return props.get(name, type);
    }

    private <T> T getInheritedPageProperty(String name, Class<T> type) {
        Resource content = getPageContentResource();
        if (content == null || content.getPath() == null) {
            return getPageProperty(name, type);
        }
        InheritanceValueMap inheritance = new HierarchyNodeInheritanceValueMap(content);
        T inherited = inheritance.getInherited(name, type);
        if (inherited != null) {
            return inherited;
        }
        return getPageProperty(name, type);
    }

    private String getPagePath() {
        Resource page = getPageResource();
        if (page == null) {
            return null;
        }
        String path = page.getPath();
        if (path == null && page.getParent() != null) {
            return page.getParent().getPath();
        }
        return path;
    }

    private String truncateToLength(String text, int maxLength) {
        if (StringUtils.isBlank(text)) {
            return StringUtils.EMPTY;
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    private String generateCanonicalFromPath() {
        String path = getPagePath();
        if (StringUtils.isBlank(path)) {
            return StringUtils.EMPTY;
        }
        return toAbsoluteUrl(path + ".html");
    }

    private String toAbsoluteUrl(String urlOrPath) {
        if (StringUtils.isBlank(urlOrPath)) {
            return StringUtils.EMPTY;
        }
        if (urlOrPath.startsWith("http://") || urlOrPath.startsWith("https://")) {
            return urlOrPath;
        }
        String path = urlOrPath.startsWith("/") ? urlOrPath : "/" + urlOrPath;
        ResourceResolver resolver = request != null ? request.getResourceResolver() : null;
        if (externalizer != null && resolver != null) {
            return externalizer.publishLink(resolver, path);
        }
        String domain = normalizeDomain(getConfiguredDomain());
        return domain + path;
    }

    private String getConfiguredDomain() {
        String domain = getInheritedPageProperty("siteDomain", String.class);
        if (StringUtils.isBlank(domain)) {
            domain = getInheritedPageProperty("canonicalDomain", String.class);
        }
        return StringUtils.defaultIfBlank(domain, DEFAULT_DOMAIN);
    }

    private String normalizeDomain(String domain) {
        if (StringUtils.isBlank(domain)) {
            return DEFAULT_DOMAIN;
        }
        return domain.endsWith("/") ? domain.substring(0, domain.length() - 1) : domain;
    }

    private Resource getPageResource() {
        Resource resource = request != null ? request.getResource() : null;
        while (resource != null) {
            ValueMap vm = resource.getValueMap();
            String primaryType = vm != null ? vm.get("jcr:primaryType", String.class) : null;
            if ("cq:Page".equals(primaryType)) {
                return resource;
            }
            resource = resource.getParent();
        }
        return null;
    }

    private Resource getPageContentResource() {
        Resource page = getPageResource();
        if (page == null) {
            return null;
        }
        Resource content = page.getChild("jcr:content");
        return content != null ? content : page;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
