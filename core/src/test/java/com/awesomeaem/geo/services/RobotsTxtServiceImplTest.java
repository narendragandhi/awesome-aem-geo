package com.awesomeaem.geo.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.awesomeaem.geo.services.impl.RobotsTxtServiceImpl;

@DisplayName("RobotsTxtServiceImpl - Unit Tests")
class RobotsTxtServiceImplTest {

    @Test
    @DisplayName("Config should override bot list and crawl delay")
    void configOverridesBotsAndDelay() throws Exception {
        RobotsTxtServiceImpl service = new RobotsTxtServiceImpl();
        RobotsTxtServiceImpl.Config config = new RobotsTxtServiceImpl.Config() {
            @Override
            public String[] aiBotUserAgents() {
                return new String[] {"TestBot"};
            }

            @Override
            public int aiBotCrawlDelay() {
                return 7;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return RobotsTxtServiceImpl.Config.class;
            }
        };

        Method activate = RobotsTxtServiceImpl.class.getDeclaredMethod("activate", RobotsTxtServiceImpl.Config.class);
        activate.setAccessible(true);
        activate.invoke(service, config);

        String robots = service.generateRobotsTxt("https://example.com");
        assertTrue(robots.contains("User-agent: TestBot"));
        assertTrue(robots.contains("Crawl-delay: 7"));
        assertTrue(service.getCrawlDelay("TestBot") == 7);
        assertTrue(service.getCrawlDelay("OtherBot") == 0);
    }

    @Test
    @DisplayName("Disallow /libs/ should block matching paths")
    void disallowLibsBlocksPath() {
        RobotsTxtServiceImpl service = new RobotsTxtServiceImpl();
        boolean allowed = service.isPathAllowed("SomeBot", "/libs/system/config");
        assertFalse(allowed);
    }
}
