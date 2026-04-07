package com.awesomeaem.geo.services;

import java.util.List;

/**
 * Sitemap Generator Service Interface
 * 
 * Provides methods to generate XML sitemaps for search engines and AI crawlers.
 */
public interface SitemapGeneratorService {

    /**
     * Generate XML path
     * 
     * @ sitemap for a rootparam rootPath the root path to generate sitemap from
     * @param maxUrls maximum number of URLs to include
     * @return XML sitemap string
     */
    String generateSitemap(String rootPath, int maxUrls);

    /**
     * Generate sitemap index for multiple sitemaps
     * 
     * @param sitemapUrls list of sitemap URLs
     * @return XML sitemap index string
     */
    String generateSitemapIndex(List<String> sitemapUrls);

    /**
     * Check if a page should be included in sitemap
     * 
     * @param pagePath the page path
     * @param properties page properties
     * @return true if should be included
     */
    boolean shouldIncludePage(String pagePath, java.util.Map<String, Object> properties);

    /**
     * Get change frequency for a page based on content type
     * 
     * @param pagePath the page path
     * @return change frequency string
     */
    String getChangeFrequency(String pagePath);

    /**
     * Get priority for a page based on depth
     * 
     * @param pagePath the page path
     * @return priority 0.0-1.0
     */
    double getPriority(String pagePath);
}
