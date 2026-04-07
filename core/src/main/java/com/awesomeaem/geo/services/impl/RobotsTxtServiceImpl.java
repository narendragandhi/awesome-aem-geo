package com.awesomeaem.geo.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.awesomeaem.geo.services.RobotsTxtService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of RobotsTxtService.
 * Generates robots.txt with AI bot support.
 * 
 * @see RobotsTxtService
 */
@Slf4j
@Component(service = RobotsTxtService.class)
@Designate(ocd = RobotsTxtServiceImpl.Config.class)
public class RobotsTxtServiceImpl implements RobotsTxtService {

    private static final String DEFAULT_ALLOW = "/content/";
    private static final String DEFAULT_DISALLOW = "/";
    private static final List<String> DEFAULT_AI_BOTS = List.of(
        "GPTBot", "ChatGPT-User", "ClaudeBot", "Claude-Web",
        "Google-Extended", "PerplexityBot", "Bytespider",
        "Amazonbot", "OAI-SearchBot", "Applebot", "DuckAssistBot"
    );

    @ObjectClassDefinition(name = "Awesome AEM GEO - Robots.txt Configuration")
    public @interface Config {
        @AttributeDefinition(name = "AI bot user-agents", description = "List of AI/LLM bot user-agents")
        String[] aiBotUserAgents() default {};

        @AttributeDefinition(name = "Default crawl delay", description = "Crawl delay for AI bots")
        int aiBotCrawlDelay() default 1;
    }

    private List<String> aiBotUserAgents = DEFAULT_AI_BOTS;
    private int aiBotCrawlDelay = 1;

    @Activate
    @Modified
    protected void activate(Config config) {
        if (config != null && config.aiBotUserAgents().length > 0) {
            aiBotUserAgents = List.of(config.aiBotUserAgents());
        } else {
            aiBotUserAgents = DEFAULT_AI_BOTS;
        }
        aiBotCrawlDelay = config != null ? config.aiBotCrawlDelay() : 1;
    }

    @Override
    public String generateRobotsTxt(String domain) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("# Robots.txt for AEM GEO - LLM Optimization\n");
        sb.append("# Generated automatically\n\n");
        
        sb.append("User-agent: *\n");
        sb.append("Allow: ").append(DEFAULT_ALLOW).append("\n");
        sb.append("Disallow: /libs/\n");
        sb.append("Disallow: /etc/\n");
        sb.append("Disallow: /bin/\n");
        sb.append("Disallow: /var/\n");
        sb.append("Crawl-delay: ").append(aiBotCrawlDelay).append("\n\n");
        
        for (String bot : aiBotUserAgents) {
            sb.append("User-agent: ").append(bot).append("\n");
            sb.append("Allow: /content/\n");
            sb.append("Disallow: /libs/\n");
            sb.append("Disallow: /etc.clientlibs/\n");
            sb.append("Disallow: /bin/\n");
            sb.append("Crawl-delay: ").append(aiBotCrawlDelay).append("\n\n");
        }
        
        sb.append("# Sitemap reference\n");
        sb.append("Sitemap: ").append(domain != null ? domain : "https://example.com");
        sb.append("/sitemap.xml\n");
        
        return sb.toString();
    }

    @Override
    public List<RobotsRule> getRules() {
        List<RobotsRule> rules = new ArrayList<>();
        
        rules.add(new RobotsRule("*", "Allow", "/content/", 1));
        rules.add(new RobotsRule("*", "Disallow", "/libs/", 0));
        rules.add(new RobotsRule("*", "Disallow", "/etc/", 0));
        rules.add(new RobotsRule("*", "Disallow", "/bin/", 0));
        
        for (String bot : aiBotUserAgents) {
            rules.add(new RobotsRule(bot, "Allow", "/content/", 1));
            rules.add(new RobotsRule(bot, "Disallow", "/libs/", 0));
            rules.add(new RobotsRule(bot, "Disallow", "/etc.clientlibs/", 0));
        }
        
        return rules;
    }

    @Override
    public boolean isPathAllowed(String userAgent, String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        
        List<RobotsRule> rules = getRules();
        
        String targetBot = "*";
        for (String bot : aiBotUserAgents) {
            if (userAgent != null && userAgent.toLowerCase().contains(bot.toLowerCase())) {
                targetBot = bot;
                break;
            }
        }

        RobotsRule bestRule = null;
        int bestLength = -1;

        for (RobotsRule rule : rules) {
            if (!rule.userAgent().equals(targetBot) && !rule.userAgent().equals("*")) {
                continue;
            }
            if (!matchesPath(path, rule.pathPattern())) {
                continue;
            }
            int length = rule.pathPattern() != null ? rule.pathPattern().length() : 0;
            boolean currentAllow = "Allow".equalsIgnoreCase(rule.directive());
            boolean bestAllow = bestRule != null && "Allow".equalsIgnoreCase(bestRule.directive());
            if (length > bestLength || (length == bestLength && currentAllow && !bestAllow)) {
                bestRule = rule;
                bestLength = length;
            }
        }

        if (bestRule == null) {
            return true;
        }
        return "Allow".equalsIgnoreCase(bestRule.directive());
    }

    @Override
    public int getCrawlDelay(String userAgent) {
        if (userAgent == null) {
            return 0;
        }
        
        for (String bot : aiBotUserAgents) {
            if (userAgent.toLowerCase().contains(bot.toLowerCase())) {
                return aiBotCrawlDelay;
            }
        }
        
        return 0;
    }

    private boolean matchesPath(String path, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return false;
        }
        if (pattern.endsWith("/")) {
            return path.startsWith(pattern);
        }
        return path.equals(pattern);
    }
}
