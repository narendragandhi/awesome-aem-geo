package com.awesomeaem.geo.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.awesomeaem.geo.services.impl.RobotsTxtServiceImpl;

@DisplayName("RobotsTxtService - SPEC Tests")
@ExtendWith(MockitoExtension.class)
class RobotsTxtSpecTest {

    private final RobotsTxtService service = new RobotsTxtServiceImpl();

    @Nested
    @DisplayName("Requirement 1: Generate Valid robots.txt")
    class GenerateRobotsTxt {
        
        @Test
        @DisplayName("Should generate valid robots.txt format")
        void should_generate_valid_format() {
            String result = service.generateRobotsTxt("https://example.com");
            
            assertNotNull(result);
            assertTrue(result.contains("User-agent: *"));
            assertTrue(result.contains("Sitemap:"));
        }

        @Test
        @DisplayName("Should include sitemap reference")
        void should_include_sitemap() {
            String result = service.generateRobotsTxt("https://example.com");
            
            assertTrue(result.contains("Sitemap: https://example.com/sitemap.xml"));
        }

        @Test
        @DisplayName("Should include AI bot rules")
        void should_include_ai_bot_rules() {
            String result = service.generateRobotsTxt("https://example.com");
            
            assertTrue(result.contains("User-agent: GPTBot"));
            assertTrue(result.contains("User-agent: ClaudeBot"));
        }

        @Test
        @DisplayName("Should include default rules for all bots")
        void should_include_default_rules() {
            String result = service.generateRobotsTxt("https://example.com");
            
            assertTrue(result.contains("Allow: /content/"));
            assertTrue(result.contains("Disallow: /libs/"));
        }
    }

    @Nested
    @DisplayName("Requirement 2: Path Allowance")
    class PathAllowance {
        
        @Test
        @DisplayName("Should allow content paths for GPTBot")
        void should_allow_content_for_gptbot() {
            assertTrue(service.isPathAllowed("GPTBot/1.0", "/content/page.html"));
        }

        @Test
        @DisplayName("Should allow content paths for ClaudeBot")
        void should_allow_content_for_claudebot() {
            assertTrue(service.isPathAllowed("ClaudeBot/2.0", "/content/blog/article.html"));
        }

        @Test
        @DisplayName("Should disallow libs paths")
        void should_disallow_libs() {
            assertFalse(service.isPathAllowed("GPTBot/1.0", "/libs/clientlibs/js.txt"));
        }

        @Test
        @DisplayName("Should disallow etc paths")
        void should_disallow_etc() {
            assertFalse(service.isPathAllowed("GPTBot/1.0", "/etc/clientlibs/"));
        }

        @Test
        @DisplayName("Should allow content for regular user agent")
        void should_allow_for_regular_ua() {
            assertTrue(service.isPathAllowed("Mozilla/5.0", "/content/page.html"));
        }
    }

    @Nested
    @DisplayName("Requirement 3: Crawl Delay")
    class CrawlDelay {
        
        @Test
        @DisplayName("Should return crawl delay for GPTBot")
        void should_return_delay_for_gptbot() {
            assertEquals(1, service.getCrawlDelay("GPTBot/1.0"));
        }

        @Test
        @DisplayName("Should return crawl delay for ClaudeBot")
        void should_return_delay_for_claudebot() {
            assertEquals(1, service.getCrawlDelay("ClaudeBot/2.0"));
        }

        @Test
        @DisplayName("Should return 0 delay for regular browser")
        void should_return_zero_for_browser() {
            assertEquals(0, service.getCrawlDelay("Mozilla/5.0"));
        }

        @Test
        @DisplayName("Should return 0 delay for null user agent")
        void should_return_zero_for_null() {
            assertEquals(0, service.getCrawlDelay(null));
        }
    }

    @Nested
    @DisplayName("Requirement 4: Get Rules")
    class GetRules {
        
        @Test
        @DisplayName("Should return list of rules")
        void should_return_rules() {
            List<RobotsTxtService.RobotsRule> rules = service.getRules();
            
            assertNotNull(rules);
            assertFalse(rules.isEmpty());
        }

        @Test
        @DisplayName("Should include AI bot rules")
        void should_include_ai_rules() {
            List<RobotsTxtService.RobotsRule> rules = service.getRules();
            
            boolean hasGptBot = rules.stream()
                .anyMatch(r -> r.userAgent().equals("GPTBot"));
            
            assertTrue(hasGptBot);
        }
    }

    @Nested
    @DisplayName("Requirement 5: Edge Cases")
    class EdgeCases {
        
        @Test
        @DisplayName("Should handle empty path")
        void should_handle_empty_path() {
            assertFalse(service.isPathAllowed("GPTBot/1.0", ""));
        }

        @Test
        @DisplayName("Should handle null user agent")
        void should_handle_null_ua() {
            assertTrue(service.isPathAllowed(null, "/content/page.html"));
        }

        @Test
        @DisplayName("Should handle unknown bot")
        void should_handle_unknown_bot() {
            assertTrue(service.isPathAllowed("UnknownBot/1.0", "/content/page.html"));
        }
    }
}
