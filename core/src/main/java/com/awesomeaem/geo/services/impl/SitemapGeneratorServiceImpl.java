package com.awesomeaem.geo.services.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.awesomeaem.geo.services.SitemapGeneratorService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of Sitemap Generator Service
 * 
 * Generates XML sitemaps from publishable AEM pages.
 */
@Slf4j
@Component(service = SitemapGeneratorService.class)
public class SitemapGeneratorServiceImpl implements SitemapGeneratorService {

    private static final String SITEMAP_NAMESPACE = "http://www.sitemaps.org/schemas/sitemap/0.9";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private static final String DEFAULT_DOMAIN = "https://www.example.com";

    private static final int DEFAULT_MAX_URLS = 50000;

    private static final String SUBSERVICE_NAME = "sitemap-service";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Externalizer externalizer;

    @Override
    public String generateSitemap(String rootPath, int maxUrls) {
        if (StringUtils.isBlank(rootPath)) {
            rootPath = "/content";
        }
        if (maxUrls <= 0) {
            maxUrls = DEFAULT_MAX_URLS;
        } else {
            maxUrls = Math.min(maxUrls, DEFAULT_MAX_URLS);
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"").append(SITEMAP_NAMESPACE).append("\">\n");

        List<SitemapUrl> urls = generateUrlList(rootPath, maxUrls);
        
        for (SitemapUrl url : urls) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(escapeXml(url.getLoc())).append("</loc>\n");
            if (url.getLastmod() != null) {
                xml.append("    <lastmod>").append(url.getLastmod()).append("</lastmod>\n");
            }
            if (url.getChangefreq() != null) {
                xml.append("    <changefreq>").append(url.getChangefreq()).append("</changefreq>\n");
            }
            if (url.getPriority() > 0) {
                xml.append("    <priority>").append(String.format("%.1f", url.getPriority())).append("</priority>\n");
            }
            xml.append("  </url>\n");
        }

        xml.append("</urlset>");

        return xml.toString();
    }

    @Override
    public String generateSitemapIndex(List<String> sitemapUrls) {
        if (sitemapUrls == null || sitemapUrls.isEmpty()) {
            return "";
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<sitemapindex xmlns=\"").append(SITEMAP_NAMESPACE).append("\">\n");

        for (String url : sitemapUrls) {
            xml.append("  <sitemap>\n");
            xml.append("    <loc>").append(escapeXml(url)).append("</loc>\n");
            xml.append("    <lastmod>").append(DATE_FORMAT.format(LocalDate.now(ZoneOffset.UTC))).append("</lastmod>\n");
            xml.append("  </sitemap>\n");
        }

        xml.append("</sitemapindex>");

        return xml.toString();
    }

    @Override
    public boolean shouldIncludePage(String pagePath, Map<String, Object> properties) {
        if (StringUtils.isBlank(pagePath)) {
            return false;
        }

        // Exclude if noindex is set
        if (properties != null) {
            Object noIndex = properties.get("noIndex");
            if (noIndex instanceof Boolean && (Boolean) noIndex) {
                return false;
            }
        }

        // Exclude certain paths
        if (pagePath.contains("/jcr:") || pagePath.contains("/libs/") || 
            pagePath.contains("/bin/") || pagePath.contains("/system/")) {
            return false;
        }

        return true;
    }

    @Override
    public String getChangeFrequency(String pagePath) {
        if (StringUtils.isBlank(pagePath)) {
            return "weekly";
        }

        String lowerPath = pagePath.toLowerCase();

        if (lowerPath.contains("/news/") || lowerPath.contains("/blog/")) {
            return "weekly";
        }
        if (lowerPath.contains("/products/")) {
            return "weekly";
        }
        if (lowerPath.contains("/about/") || lowerPath.contains("/company/")) {
            return "monthly";
        }

        return "weekly";
    }

    @Override
    public double getPriority(String pagePath) {
        if (StringUtils.isBlank(pagePath)) {
            return 0.5;
        }

        String lowerPath = pagePath.toLowerCase();

        // Home page gets highest priority
        if (lowerPath.endsWith("/content/site/en") || lowerPath.endsWith("/content/site/en.html")) {
            return 1.0;
        }

        // Count path depth
        int depth = StringUtils.countMatches(pagePath, "/");
        
        // Level 1 (e.g., /content/site/en/about)
        if (depth <= 4) {
            return 0.8;
        }
        // Level 2
        if (depth <= 5) {
            return 0.6;
        }
        // Level 3
        if (depth <= 6) {
            return 0.4;
        }
        // Deeper pages
        return 0.3;
    }

    private List<SitemapUrl> generateUrlList(String rootPath, int maxUrls) {
        List<SitemapUrl> urls = new ArrayList<>();
        try (ResourceResolver resolver = getServiceResolver()) {
            if (resolver == null) {
                return urls;
            }

            PageManager pageManager = resolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                Page rootPage = pageManager.getPage(rootPath);
                if (rootPage != null) {
                    collectPages(rootPage, resolver, urls, maxUrls);
                    return urls;
                }
            }

            Resource rootResource = resolver.getResource(rootPath);
            if (rootResource == null) {
                return urls;
            }

            ArrayDeque<Resource> queue = new ArrayDeque<>();
            queue.add(rootResource);

            while (!queue.isEmpty() && urls.size() < maxUrls) {
                Resource current = queue.poll();
                if (current == null) {
                    continue;
                }

                String primaryType = current.getValueMap().get("jcr:primaryType", String.class);
                if ("cq:Page".equals(primaryType)) {
                    Resource content = current.getChild("jcr:content");
                    Map<String, Object> props = new HashMap<>();
                    if (content != null) {
                        props.putAll(content.getValueMap());
                    }
                    if (shouldIncludePage(current.getPath(), props)) {
                        urls.add(buildUrl(current.getPath(), resolver, props));
                    }
                }

                current.getChildren().forEach(queue::add);
            }
        }

        return urls;
    }

    private ResourceResolver getServiceResolver() {
        if (resourceResolverFactory == null) {
            return null;
        }
        try {
            return resourceResolverFactory.getServiceResourceResolver(
                Map.of(ResourceResolverFactory.SUBSERVICE, SUBSERVICE_NAME)
            );
        } catch (LoginException e) {
            log.warn("Unable to obtain service resolver for sitemap generation", e);
            return null;
        }
    }

    private void collectPages(Page rootPage, ResourceResolver resolver, List<SitemapUrl> urls, int maxUrls) {
        ArrayDeque<Page> queue = new ArrayDeque<>();
        queue.add(rootPage);

        while (!queue.isEmpty() && urls.size() < maxUrls) {
            Page current = queue.poll();
            if (current == null) {
                continue;
            }

            Resource content = current.getContentResource();
            Map<String, Object> props = new HashMap<>();
            if (content != null) {
                props.putAll(content.getValueMap());
            }
            if (shouldIncludePage(current.getPath(), props)) {
                urls.add(buildUrl(current.getPath(), resolver, props));
            }

            current.listChildren().forEachRemaining(queue::add);
        }
    }

    private SitemapUrl buildUrl(String pagePath, ResourceResolver resolver, Map<String, Object> properties) {
        String loc = toAbsoluteUrl(pagePath + ".html", resolver);
        String lastmod = getLastModified(properties);
        return new SitemapUrl.Builder()
            .loc(loc)
            .lastmod(lastmod)
            .changefreq(getChangeFrequency(pagePath))
            .priority(getPriority(pagePath))
            .build();
    }

    private String getLastModified(Map<String, Object> properties) {
        Calendar lastMod = null;
        Object lastModifiedObj = properties.get("jcr:lastModified");
        if (lastModifiedObj instanceof Calendar) {
            lastMod = (Calendar) lastModifiedObj;
        }
        if (lastMod == null) {
            Object createdObj = properties.get("jcr:created");
            if (createdObj instanceof Calendar) {
                lastMod = (Calendar) createdObj;
            }
        }
        if (lastMod == null) {
            return null;
        }
        Instant instant = lastMod.toInstant();
        return DATE_FORMAT.format(instant.atZone(ZoneOffset.UTC).toLocalDate());
    }

    private String toAbsoluteUrl(String urlOrPath, ResourceResolver resolver) {
        if (StringUtils.isBlank(urlOrPath)) {
            return "";
        }
        if (urlOrPath.startsWith("http://") || urlOrPath.startsWith("https://")) {
            return urlOrPath;
        }
        String path = urlOrPath.startsWith("/") ? urlOrPath : "/" + urlOrPath;
        if (externalizer != null && resolver != null) {
            return externalizer.publishLink(resolver, path);
        }
        return DEFAULT_DOMAIN + path;
    }

    private String escapeXml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }

    /**
     * Sitemap URL entry
     */
    public static class SitemapUrl {
        private final String loc;
        private final String lastmod;
        private final String changefreq;
        private final double priority;

        private SitemapUrl(Builder builder) {
            this.loc = builder.loc;
            this.lastmod = builder.lastmod;
            this.changefreq = builder.changefreq;
            this.priority = builder.priority;
        }

        public String getLoc() { return loc; }
        public String getLastmod() { return lastmod; }
        public String getChangefreq() { return changefreq; }
        public double getPriority() { return priority; }

        public static class Builder {
            private String loc;
            private String lastmod;
            private String changefreq = "weekly";
            private double priority = 0.5;

            public Builder loc(String loc) { this.loc = loc; return this; }
            public Builder lastmod(String lastmod) { this.lastmod = lastmod; return this; }
            public Builder changefreq(String changefreq) { this.changefreq = changefreq; return this; }
            public Builder priority(double priority) { this.priority = priority; return this; }
            public SitemapUrl build() { return new SitemapUrl(this); }
        }
    }
}
