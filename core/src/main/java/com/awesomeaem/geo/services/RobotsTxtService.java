package com.awesomeaem.geo.services;

import java.util.List;

/**
 * Service for generating robots.txt with AI bot support.
 * 
 * <p>Generates robots.txt that includes rules for both traditional search bots
 * and AI/LLM bots (GPTBot, ClaudeBot, PerplexityBot, etc.)</p>
 * 
 * @since 1.0.0
 */
public interface RobotsTxtService {
    
    /**
     * Generate robots.txt content.
     * @param domain Website domain
     * @return robots.txt content
     */
    String generateRobotsTxt(String domain);
    
    /**
     * Get list of robots.txt rules.
     * @return List of rules
     */
    List<RobotsRule> getRules();
    
    /**
     * Check if a path is allowed for a user agent.
     * @param userAgent User-agent string
     * @param path      Path to check
     * @return true if allowed
     */
    boolean isPathAllowed(String userAgent, String path);
    
    /**
     * Get crawl delay for a user agent.
     * @param userAgent User-agent string
     * @return Crawl delay in seconds
     */
    int getCrawlDelay(String userAgent);

    /**
     * Represents a robots.txt rule.
     * @param userAgent    User-agent pattern
     * @param directive    Directive (Allow/Disallow)
     * @param pathPattern Path pattern
     * @param crawlDelay  Crawl delay in seconds
     */
    record RobotsRule(
        String userAgent,
        String directive,
        String pathPattern,
        int crawlDelay
    ) {}
}
