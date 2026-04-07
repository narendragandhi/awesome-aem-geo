package com.awesomeaem.geo.services.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

import com.awesomeaem.geo.services.AiAnalyticsService;

/**
 * Implementation of AiAnalyticsService.
 * Tracks AI/LLM bot visits and provides analytics.
 * 
 * @see AiAnalyticsService
 */
@Component(service = AiAnalyticsService.class)
public class AiAnalyticsServiceImpl implements AiAnalyticsService {

    private final ConcurrentLinkedQueue<BotVisit> visitStore = new ConcurrentLinkedQueue<>();
    private static final int MAX_STORE_SIZE = 10000;

    @Override
    public void recordVisit(BotVisit visit) {
        if (visit == null) {
            return;
        }

        BotVisit normalizedVisit = new BotVisit(
            normalizeBotName(visit.botName()),
            visit.userAgent(),
            visit.timestamp(),
            visit.requestedPath(),
            visit.responseTimeMs(),
            visit.statusCode()
        );
        
        visitStore.add(normalizedVisit);

        while (visitStore.size() > MAX_STORE_SIZE) {
            visitStore.poll();
        }
    }

    @Override
    public AnalyticsSummary getSummary(String timeRange) {
        List<BotVisit> filtered = filterByTimeRange(timeRange);
        
        Map<String, Integer> botCounts = new HashMap<>();
        Map<String, Integer> pageCounts = new HashMap<>();
        
        Instant firstVisit = null;
        Instant lastVisit = null;
        
        for (BotVisit visit : filtered) {
            String botName = normalizeBotName(visit.botName());
            botCounts.merge(botName, 1, Integer::sum);
            pageCounts.merge(visit.requestedPath(), 1, Integer::sum);
            
            if (firstVisit == null || visit.timestamp().isBefore(firstVisit)) {
                firstVisit = visit.timestamp();
            }
            if (lastVisit == null || visit.timestamp().isAfter(lastVisit)) {
                lastVisit = visit.timestamp();
            }
        }
        
        Map<String, Integer> topPages = pageCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                HashMap::new
            ));
        
        return new AnalyticsSummary(
            filtered.size(),
            botCounts,
            topPages,
            firstVisit,
            lastVisit
        );
    }

    @Override
    public List<BotVisit> getRecentVisits(int limit) {
        return visitStore.stream()
            .skip(Math.max(0, visitStore.size() - limit))
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getBotBreakdown() {
        Map<String, Integer> counts = new HashMap<>();
        
        for (BotVisit visit : visitStore) {
            String botName = normalizeBotName(visit.botName());
            counts.merge(botName, 1, Integer::sum);
        }
        
        return counts;
    }

    @Override
    public List<String> getTopPages(int limit) {
        Map<String, Integer> pageCounts = new HashMap<>();
        
        for (BotVisit visit : visitStore) {
            if (StringUtils.isNotBlank(visit.requestedPath())) {
                pageCounts.merge(visit.requestedPath(), 1, Integer::sum);
            }
        }
        
        return pageCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        visitStore.clear();
    }

    private List<BotVisit> filterByTimeRange(String timeRange) {
        if (StringUtils.isBlank(timeRange)) {
            return new ArrayList<>(visitStore);
        }
        
        Instant cutoff = switch (timeRange.toLowerCase()) {
            case "1h" -> Instant.now().minusSeconds(3600);
            case "24h", "1d" -> Instant.now().minusSeconds(86400);
            case "7d", "1w" -> Instant.now().minusSeconds(604800);
            case "30d", "1m" -> Instant.now().minusSeconds(2592000);
            default -> Instant.MIN;
        };
        
        if (cutoff == Instant.MIN) {
            return new ArrayList<>(visitStore);
        }
        
        return visitStore.stream()
            .filter(v -> v.timestamp().isAfter(cutoff))
            .collect(Collectors.toList());
    }

    private String normalizeBotName(String botName) {
        if (StringUtils.isBlank(botName)) {
            return "Unknown";
        }
        return botName.trim();
    }
}
