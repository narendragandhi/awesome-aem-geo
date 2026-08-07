package com.awesomeaem.geo.services.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

import com.awesomeaem.geo.services.AiBotHandlerService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of AiBotHandlerService.
 * Detects and handles AI/LLM bot requests.
 * 
 * @see AiBotHandlerService
 */
@Slf4j
@Component(service = AiBotHandlerService.class)
public class AiBotHandlerServiceImpl implements AiBotHandlerService {

    private static final Map<String, String> AI_BOT_PATTERNS = new ConcurrentHashMap<>();
    static {
        AI_BOT_PATTERNS.put("GPTBot", "OpenAI");
        AI_BOT_PATTERNS.put("ChatGPT-User", "OpenAI");
        AI_BOT_PATTERNS.put("ClaudeBot", "Anthropic");
        AI_BOT_PATTERNS.put("Claude-Web", "Anthropic");
        AI_BOT_PATTERNS.put("Claude-SearchBot", "Anthropic");
        AI_BOT_PATTERNS.put("Claude-User", "Anthropic");
        AI_BOT_PATTERNS.put("Google-Extended", "Google");
        AI_BOT_PATTERNS.put("PerplexityBot", "Perplexity");
        AI_BOT_PATTERNS.put("Bytespider", "ByteDance");
        AI_BOT_PATTERNS.put("Amazonbot", "Amazon");
        AI_BOT_PATTERNS.put("OAI-SearchBot", "OpenAI");
        AI_BOT_PATTERNS.put("Applebot", "Apple");
        AI_BOT_PATTERNS.put("DuckAssistBot", "DuckDuckGo");
        AI_BOT_PATTERNS.put("FacebookBot", "Meta");
        AI_BOT_PATTERNS.put("TwitterBot", "Twitter");
        AI_BOT_PATTERNS.put("LinkedInBot", "LinkedIn");
        AI_BOT_PATTERNS.put("SlackBot", "Slack");
        AI_BOT_PATTERNS.put("Discordbot", "Discord");
    }

    private final List<AiBotVisit> visitHistory = new CopyOnWriteArrayList<>();

    @Override
    public boolean isAiBot(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String userAgent = request.getHeader("User-Agent");
        return isAiBotUserAgent(userAgent);
    }

    @Override
    public boolean isAiBotUserAgent(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }
        String lowerUa = userAgent.toLowerCase();
        for (String botPattern : AI_BOT_PATTERNS.keySet()) {
            if (lowerUa.contains(botPattern.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getBotName(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return getBotNameFromUserAgent(request.getHeader("User-Agent"));
    }

    @Override
    public String getBotNameFromUserAgent(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return null;
        }
        String lowerUa = userAgent.toLowerCase();
        for (Map.Entry<String, String> entry : AI_BOT_PATTERNS.entrySet()) {
            if (lowerUa.contains(entry.getKey().toLowerCase())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public boolean isAllowed(HttpServletRequest request) {
        return true;
    }

    @Override
    public void recordVisit(String botName, String path, Instant timestamp) {
        if (StringUtils.isNotBlank(botName) && StringUtils.isNotBlank(path)) {
            visitHistory.add(new AiBotVisit(
                botName,
                path,
                timestamp != null ? timestamp : Instant.now(),
                200
            ));
            if (visitHistory.size() > 1000) {
                visitHistory.remove(0);
            }
        }
    }

    @Override
    public List<AiBotVisit> getRecentVisits(int limit) {
        if (limit <= 0) {
            limit = 10;
        }
        int size = Math.min(limit, visitHistory.size());
        List<AiBotVisit> result = new ArrayList<>(size);
        int start = Math.max(0, visitHistory.size() - size);
        for (int i = start; i < visitHistory.size(); i++) {
            result.add(visitHistory.get(i));
        }
        return result;
    }
}
