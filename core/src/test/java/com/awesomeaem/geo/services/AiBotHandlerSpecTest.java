package com.awesomeaem.geo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.awesomeaem.geo.services.impl.AiBotHandlerServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@DisplayName("AiBotHandlerService - SPEC Tests")
@ExtendWith(MockitoExtension.class)
class AiBotHandlerSpecTest {

    private final AiBotHandlerService service = new AiBotHandlerServiceImpl();

    @Nested
    @DisplayName("Requirement 1: AI Bot Detection - GPTBot")
    class GptBotDetection {
        
        @Test
        @DisplayName("Should detect GPTBot user agent")
        void should_detect_gptbot() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("GPTBot/1.0");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should detect ChatGPT-User agent")
        void should_detect_chatgpt_user() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("ChatGPT-User/1.0");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should extract GPTBot as bot name")
        void should_extract_gptbot_name() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("GPTBot/1.0");
            
            assertEquals("GPTBot", service.getBotName(request));
        }
    }

    @Nested
    @DisplayName("Requirement 2: AI Bot Detection - ClaudeBot")
    class ClaudeBotDetection {
        
        @Test
        @DisplayName("Should detect ClaudeBot user agent")
        void should_detect_claudebot() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("ClaudeBot/2.0");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should detect Claude-Web user agent")
        void should_detect_claude_web() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("Claude-Web/1.0");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should extract ClaudeBot as bot name")
        void should_extract_claudebot_name() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("ClaudeBot/2.0");
            
            assertEquals("ClaudeBot", service.getBotName(request));
        }

        @Test
        @DisplayName("Should detect current Claude search and user agents")
        void should_detect_current_claude_agents() {
            assertTrue(service.isAiBotUserAgent("Claude-SearchBot/1.0"));
            assertTrue(service.isAiBotUserAgent("Claude-User/1.0"));
        }
    }

    @Nested
    @DisplayName("Requirement 3: AI Bot Detection - Other Bots")
    class OtherBotDetection {
        
        @Test
        @DisplayName("Should detect PerplexityBot")
        void should_detect_perplexitybot() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("PerplexityBot/1.0");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should detect Google-Extended")
        void should_detect_google_extended() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("Google-Extended");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should detect Applebot")
        void should_detect_applebot() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("Applebot/0.1");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should detect DuckAssistBot")
        void should_detect_duckassistbot() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("DuckAssistBot/1.0");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should detect Bytespider")
        void should_detect_bytespider() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("Bytespider");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should detect Amazonbot")
        void should_detect_amazonbot() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("Amazonbot/0.1");
            
            assertTrue(service.isAiBot(request));
        }
    }

    @Nested
    @DisplayName("Requirement 4: Non-AI Bot Detection")
    class NonBotDetection {
        
        @Test
        @DisplayName("Should not detect regular browser as AI bot")
        void should_not_detect_browser() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)");
            
            assertFalse(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should not detect empty user agent as AI bot")
        void should_not_detect_empty_ua() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("");
            
            assertFalse(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should not detect null user agent as AI bot")
        void should_not_detect_null_ua() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn(null);
            
            assertFalse(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should not detect generic 'bot' string as AI bot")
        void should_not_detect_generic_bot() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("GenericBot/1.0");
            
            assertFalse(service.isAiBot(request));
        }
    }

    @Nested
    @DisplayName("Requirement 5: Case Insensitivity")
    class CaseInsensitivity {
        
        @Test
        @DisplayName("Should detect bot regardless of case")
        void should_detect_case_insensitive() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("gptbot/1.0");
            
            assertTrue(service.isAiBot(request));
        }

        @Test
        @DisplayName("Should extract bot name preserving original case")
        void should_extract_name_case_preserved() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("GPTBot/1.0");
            
            assertEquals("GPTBot", service.getBotName(request));
        }
    }

    @Nested
    @DisplayName("Requirement 6: Visit Recording")
    class VisitRecording {
        
        @Test
        @DisplayName("Should record AI bot visit")
        void should_record_visit() {
            service.recordVisit("GPTBot", "/content/page.html", Instant.now());
            
            List<AiBotHandlerService.AiBotVisit> visits = service.getRecentVisits(10);
            assertFalse(visits.isEmpty());
            assertEquals("GPTBot", visits.get(0).botName());
            assertEquals("/content/page.html", visits.get(0).path());
        }

        @Test
        @DisplayName("Should limit recent visits")
        void should_limit_visits() {
            for (int i = 0; i < 15; i++) {
                service.recordVisit("GPTBot", "/content/page" + i + ".html", Instant.now());
            }
            
            List<AiBotHandlerService.AiBotVisit> visits = service.getRecentVisits(5);
            assertTrue(visits.size() <= 5);
        }

        @Test
        @DisplayName("Should return empty list when no visits")
        void should_return_empty_when_no_visits() {
            List<AiBotHandlerService.AiBotVisit> visits = service.getRecentVisits(10);
            assertTrue(visits.isEmpty());
        }
    }

    @Nested
    @DisplayName("Requirement 7: Edge Cases")
    class EdgeCases {
        
        @Test
        @DisplayName("Should handle null request")
        void should_handle_null_request() {
            assertFalse(service.isAiBot(null));
            assertNull(service.getBotName(null));
        }

        @Test
        @DisplayName("Should return null for unknown bot name")
        void should_return_null_for_unknown() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("User-Agent")).thenReturn("UnknownBot/1.0");
            
            assertNull(service.getBotName(request));
        }

        @Test
        @DisplayName("Should default to allowed")
        void should_default_to_allowed() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            
            assertTrue(service.isAllowed(request));
        }
    }
}
