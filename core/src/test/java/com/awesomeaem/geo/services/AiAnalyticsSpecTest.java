package com.awesomeaem.geo.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.awesomeaem.geo.services.impl.AiAnalyticsServiceImpl;

@DisplayName("AiAnalyticsService - SPEC Tests")
@ExtendWith(MockitoExtension.class)
class AiAnalyticsSpecTest {

    private AiAnalyticsService service;

    @BeforeEach
    void setUp() {
        service = new AiAnalyticsServiceImpl();
    }

    @Nested
    @DisplayName("Requirement 1: Record Bot Visits")
    class RecordVisits {
        
        @Test
        @DisplayName("Should record a GPTBot visit")
        void should_record_gptbot_visit() {
            var visit = new AiAnalyticsService.BotVisit(
                "GPTBot",
                "Mozilla/5.0 Applebot/0.1",
                Instant.now(),
                "/content/page1.html",
                150,
                "200"
            );
            
            service.recordVisit(visit);
            
            List<AiAnalyticsService.BotVisit> recent = service.getRecentVisits(10);
            assertFalse(recent.isEmpty());
            assertEquals("GPTBot", recent.get(0).botName());
        }

        @Test
        @DisplayName("Should record a ClaudeBot visit")
        void should_record_claudebot_visit() {
            var visit = new AiAnalyticsService.BotVisit(
                "ClaudeBot",
                "ClaudeBot/2.0",
                Instant.now(),
                "/content/blog/article",
                200,
                "200"
            );
            
            service.recordVisit(visit);
            
            List<AiAnalyticsService.BotVisit> recent = service.getRecentVisits(10);
            boolean hasClaude = recent.stream()
                .anyMatch(v -> v.botName().equals("ClaudeBot"));
            assertTrue(hasClaude);
        }

        @Test
        @DisplayName("Should record multiple visits")
        void should_record_multiple_visits() {
            for (int i = 0; i < 5; i++) {
                var visit = new AiAnalyticsService.BotVisit(
                    "GPTBot",
                    "GPTBot/1.0",
                    Instant.now(),
                    "/content/page" + i + ".html",
                    100 + i,
                    "200"
                );
                service.recordVisit(visit);
            }
            
            List<AiAnalyticsService.BotVisit> recent = service.getRecentVisits(10);
            assertEquals(5, recent.size());
        }

        @Test
        @DisplayName("Should handle null visit gracefully")
        void should_handle_null_visit() {
            assertDoesNotThrow(() -> service.recordVisit(null));
        }
    }

    @Nested
    @DisplayName("Requirement 2: Analytics Summary")
    class AnalyticsSummary {
        
        @Test
        @DisplayName("Should return summary with visit count")
        void should_return_visit_count() {
            service.recordVisit(createVisit("GPTBot", "/page1"));
            service.recordVisit(createVisit("ClaudeBot", "/page2"));
            service.recordVisit(createVisit("GPTBot", "/page3"));
            
            var summary = service.getSummary("24h");
            
            assertEquals(3, summary.totalVisits());
        }

        @Test
        @DisplayName("Should include bot breakdown")
        void should_include_bot_breakdown() {
            service.recordVisit(createVisit("GPTBot", "/p1"));
            service.recordVisit(createVisit("GPTBot", "/p2"));
            service.recordVisit(createVisit("ClaudeBot", "/p3"));
            
            Map<String, Integer> breakdown = service.getBotBreakdown();
            
            assertEquals(2, breakdown.get("GPTBot"));
            assertEquals(1, breakdown.get("ClaudeBot"));
        }

        @Test
        @DisplayName("Should handle empty data")
        void should_handle_empty_data() {
            var summary = service.getSummary("24h");
            
            assertEquals(0, summary.totalVisits());
            assertTrue(summary.botBreakdown().isEmpty());
        }
    }

    @Nested
    @DisplayName("Requirement 2a: AI Referral Visibility")
    class ReferralVisibility {

        @Test
        @DisplayName("Should count ChatGPT referrals separately from crawlers")
        void should_count_chatgpt_referrals() {
            service.recordReferral(new AiAnalyticsService.ReferralVisit(
                "ChatGPT.com", "/content/page", Instant.now()));
            service.recordVisit(createVisit("OAI-SearchBot", "/content/page"));

            assertEquals(1, service.getReferralBreakdown().get("chatgpt.com"));
            assertEquals(1, service.getBotBreakdown().get("OAI-SearchBot"));
        }

        @Test
        @DisplayName("Should ignore incomplete referrals")
        void should_ignore_incomplete_referrals() {
            service.recordReferral(null);
            service.recordReferral(new AiAnalyticsService.ReferralVisit(null, "/page", Instant.now()));

            assertTrue(service.getReferralBreakdown().isEmpty());
        }
    }

    @Nested
    @DisplayName("Requirement 3: Recent Visits")
    class RecentVisits {
        
        @Test
        @DisplayName("Should return recent visits")
        void should_return_recent_visits() {
            for (int i = 0; i < 10; i++) {
                service.recordVisit(createVisit("GPTBot", "/page" + i));
            }
            
            List<AiAnalyticsService.BotVisit> recent = service.getRecentVisits(5);
            
            assertEquals(5, recent.size());
        }

        @Test
        @DisplayName("Should return empty list when no visits")
        void should_return_empty_when_no_visits() {
            List<AiAnalyticsService.BotVisit> recent = service.getRecentVisits(10);
            
            assertTrue(recent.isEmpty());
        }

        @Test
        @DisplayName("Should respect limit parameter")
        void should_respect_limit() {
            for (int i = 0; i < 20; i++) {
                service.recordVisit(createVisit("GPTBot", "/page" + i));
            }
            
            List<AiAnalyticsService.BotVisit> recent = service.getRecentVisits(3);
            
            assertEquals(3, recent.size());
        }
    }

    @Nested
    @DisplayName("Requirement 4: Top Pages")
    class TopPages {
        
        @Test
        @DisplayName("Should return top pages")
        void should_return_top_pages() {
            service.recordVisit(createVisit("GPTBot", "/popular"));
            service.recordVisit(createVisit("GPTBot", "/popular"));
            service.recordVisit(createVisit("ClaudeBot", "/popular"));
            service.recordVisit(createVisit("PerplexityBot", "/other"));
            
            List<String> top = service.getTopPages(5);
            
            assertEquals("/popular", top.get(0));
        }

        @Test
        @DisplayName("Should order by visit count")
        void should_order_by_count() {
            service.recordVisit(createVisit("GPTBot", "/page-a"));
            service.recordVisit(createVisit("ClaudeBot", "/page-a"));
            service.recordVisit(createVisit("GPTBot", "/page-b"));
            
            List<String> top = service.getTopPages(2);
            
            assertEquals("/page-a", top.get(0));
            assertEquals("/page-b", top.get(1));
        }

        @Test
        @DisplayName("Should handle empty data")
        void should_handle_empty() {
            List<String> top = service.getTopPages(5);
            
            assertTrue(top.isEmpty());
        }
    }

    @Nested
    @DisplayName("Requirement 5: Time Range")
    class TimeRange {
        
        @Test
        @DisplayName("Should filter by time range")
        void should_filter_by_time_range() {
            var oldVisit = new AiAnalyticsService.BotVisit(
                "GPTBot", 
                "GPTBot/1.0", 
                Instant.parse("2024-01-01T00:00:00Z"),
                "/old-page", 
                100, 
                "200"
            );
            service.recordVisit(oldVisit);
            service.recordVisit(createVisit("GPTBot", "/new-page"));
            
            var summary = service.getSummary("24h");
            
            assertEquals(1, summary.totalVisits());
        }
    }

    @Nested
    @DisplayName("Requirement 6: Edge Cases")
    class EdgeCases {
        
        @Test
        @DisplayName("Should handle unknown bot")
        void should_handle_unknown_bot() {
            var visit = new AiAnalyticsService.BotVisit(
                "UnknownBot",
                "UnknownBot/1.0",
                Instant.now(),
                "/test",
                50,
                "200"
            );
            
            service.recordVisit(visit);
            
            Map<String, Integer> breakdown = service.getBotBreakdown();
            assertEquals(1, breakdown.get("UnknownBot"));
        }

        @Test
        @DisplayName("Should handle error response codes")
        void should_handle_error_codes() {
            var visit = new AiAnalyticsService.BotVisit(
                "GPTBot",
                "GPTBot/1.0",
                Instant.now(),
                "/not-found",
                100,
                "404"
            );
            
            service.recordVisit(visit);
            
            List<AiAnalyticsService.BotVisit> recent = service.getRecentVisits(1);
            assertEquals("404", recent.get(0).statusCode());
        }

        @Test
        @DisplayName("Should handle slow response times")
        void should_handle_slow_responses() {
            var visit = new AiAnalyticsService.BotVisit(
                "GPTBot",
                "GPTBot/1.0",
                Instant.now(),
                "/slow-page",
                5000,
                "200"
            );
            
            service.recordVisit(visit);
            
            List<AiAnalyticsService.BotVisit> recent = service.getRecentVisits(1);
            assertEquals(5000, recent.get(0).responseTimeMs());
        }

        @Test
        @DisplayName("Should handle null bot name")
        void should_handle_null_bot_name() {
            var visit = new AiAnalyticsService.BotVisit(
                null,
                "TestBot/1.0",
                Instant.now(),
                "/test",
                100,
                "200"
            );
            
            service.recordVisit(visit);
            
            List<AiAnalyticsService.BotVisit> recent = service.getRecentVisits(1);
            assertNotNull(recent.get(0).botName());
        }
    }

    private AiAnalyticsService.BotVisit createVisit(String bot, String path) {
        return new AiAnalyticsService.BotVisit(
            bot,
            bot + "/1.0",
            Instant.now(),
            path,
            100,
            "200"
        );
    }
}
