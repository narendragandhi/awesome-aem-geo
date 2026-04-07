package com.awesomeaem.geo.services;

import java.time.Instant;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Service for detecting and handling AI/LLM bot requests.
 * 
 * <p>Detects AI bots (GPTBot, ClaudeBot, PerplexityBot, Google-Extended, etc.)
 * and can serve optimized content for AI crawlers.</p>
 * 
 * @since 1.0.0
 */
public interface AiBotHandlerService {

    /**
     * Check if request is from an AI bot.
     * @param request HTTP request
     * @return true if AI bot detected
     */
    boolean isAiBot(HttpServletRequest request);

    /**
     * Check if user-agent string matches known AI bots.
     * @param userAgent User-agent string
     * @return true if AI bot
     */
    boolean isAiBotUserAgent(String userAgent);

    /**
     * Get AI bot name from request.
     * @param request HTTP request
     * @return Bot name or null
     */
    String getBotName(HttpServletRequest request);

    /**
     * Get AI bot name from user-agent string.
     * @param userAgent User-agent string
     * @return Bot name or null
     */
    String getBotNameFromUserAgent(String userAgent);

    /**
     * Check if AI bot is allowed to access resource.
     * @param request HTTP request
     * @return true if allowed
     */
    boolean isAllowed(HttpServletRequest request);

    /**
     * Record a visit from an AI bot.
     * @param botName  Name of bot
     * @param path     Path requested
     * @param timestamp Visit time
     */
    void recordVisit(String botName, String path, Instant timestamp);

    /**
     * Get recent AI bot visits.
     * @param limit Maximum visits to return
     * @return List of recent visits
     */
    List<AiBotVisit> getRecentVisits(int limit);

    /**
     * Represents an AI bot visit.
     * @param botName       Bot name
     * @param path          Path requested
     * @param timestamp     Visit timestamp
     * @param responseStatus HTTP response status
     */
    record AiBotVisit(
        String botName,
        String path,
        Instant timestamp,
        int responseStatus
    ) {}
}
