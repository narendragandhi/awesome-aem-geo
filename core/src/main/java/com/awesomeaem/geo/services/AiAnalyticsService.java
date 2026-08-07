package com.awesomeaem.geo.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Service for tracking AI/LLM bot analytics.
 * 
 * <p>Records visits from AI bots (GPTBot, ClaudeBot, PerplexityBot, etc.)
 * and provides analytics for understanding AI traffic patterns.</p>
 * 
 * @since 1.0.0
 */
public interface AiAnalyticsService {

    /**
     * Represents a single visit from an AI bot.
     * 
     * @param botName      Name of the AI bot (e.g., "GPTBot")
     * @param userAgent    Full user-agent string
     * @param timestamp    Time of the visit
     * @param requestedPath Path that was requested
     * @param responseTimeMs Response time in milliseconds
     * @param statusCode   HTTP status code returned
     */
    record BotVisit(
        String botName,
        String userAgent,
        Instant timestamp,
        String requestedPath,
        int responseTimeMs,
        String statusCode
    ) {}

    /**
     * Summary statistics for AI bot visits.
     * 
     * @param totalVisits  Total number of visits in the time range
     * @param botBreakdown Map of bot names to visit counts
     * @param topPages     Map of page paths to visit counts
     * @param firstVisit   Timestamp of first visit
     * @param lastVisit    Timestamp of most recent visit
     */
    record AnalyticsSummary(
        int totalVisits,
        Map<String, Integer> botBreakdown,
        Map<String, Integer> topPages,
        Instant firstVisit,
        Instant lastVisit
    ) {}

    /**
     * Represents a referral from an AI search product.
     */
    record ReferralVisit(
        String source,
        String requestedPath,
        Instant timestamp
    ) {}

    /**
     * Record a bot visit.
     * 
     * @param visit The visit to record
     */
    void recordVisit(BotVisit visit);

    /**
     * Record a user referral from an AI search product.
     *
     * @param referral referral event
     */
    void recordReferral(ReferralVisit referral);

    /**
     * Get referral counts by source.
     *
     * @return source-to-count map
     */
    Map<String, Integer> getReferralBreakdown();

    /**
     * Get analytics summary for a time range.
     * 
     * @param timeRange Time range (e.g., "1h", "24h", "7d", "30d")
     * @return Analytics summary
     */
    AnalyticsSummary getSummary(String timeRange);

    /**
     * Get recent bot visits.
     * 
     * @param limit Maximum number of visits to return
     * @return List of recent visits
     */
    List<BotVisit> getRecentVisits(int limit);

    /**
     * Get breakdown of visits by bot type.
     * 
     * @return Map of bot names to visit counts
     */
    Map<String, Integer> getBotBreakdown();

    /**
     * Get most visited pages by AI bots.
     * 
     * @param limit Maximum number of pages to return
     * @return List of page paths sorted by visit count
     */
    List<String> getTopPages(int limit);

    /**
     * Clear all stored analytics data.
     */
    void clear();
}
