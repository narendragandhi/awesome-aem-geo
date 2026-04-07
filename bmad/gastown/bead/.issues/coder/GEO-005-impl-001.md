---
id: GEO-005-impl-001
workflow_id: GEO-005
type: implementation
agent: coder
status: pending
priority: high
created: 2024-03-11T17:30:00Z
updated: 2024-03-11T17:30:00Z
depends_on: [GEO-005-spec-001]
blocks: [GEO-005-test-001, GEO-005-review-001]
---

# Implement Robots.txt Generator Component

## Context

Implement the Robots.txt Generator as specified in GEO-005-spec-001.

**Component Type**: OSGi Service + Servlet
**Reference**: bmad/gastown/bead/.issues/docs/GEO-005-spec-001.md

## Specification Summary

Dynamically generate robots.txt with AI-specific directives. Supports:
- AI bot user-agent rules
- Allow/disallow path patterns
- Crawl-delay rate limiting
- Sitemap reference

## Acceptance Criteria

- [ ] RobotsTxtService interface created
- [ ] RobotsTxtServiceImpl implementation
- [ ] RobotsTxtServlet handles /robots.txt
- [ ] AI bot rules working
- [ ] Sitemap reference included
- [ ] Code compiles without errors

## Technical Details

### Service Interface

```java
public interface RobotsTxtService {
    String generateRobotsTxt(String domain);
    List<RobotsRule> getRules();
    boolean isPathAllowed(String userAgent, String path);
    int getCrawlDelay(String userAgent);
}
```

### AI Bot User Agents

| Bot | User-Agent |
|-----|------------|
| OpenAI | GPTBot, ChatGPT-User |
| Anthropic | ClaudeBot, Claude-Web |
| Perplexity | PerplexityBot |
| Google | Google-Extended |
| Others | Applebot, DuckAssistBot |

### File Locations

| File Type | Path |
|-----------|------|
| Service Interface | `core/src/main/java/com/awesomeaem/geo/services/RobotsTxtService.java` |
| Service Implementation | `core/src/main/java/com/awesomeaem/geo/services/impl/RobotsTxtServiceImpl.java` |
| Servlet | `core/src/main/java/com/awesomeaem/geo/servlets/RobotsTxtServlet.java` |

## Progress Log

### 2024-03-11
Issue created.

## Handoff Notes

- Cache generated output for 1 hour
- Use StringBuilder for efficient concatenation
- Test with multiple AI bot user agents

## Related Issues

- Specification: #GEO-005-spec-001
- Testing: #GEO-005-test-001
- Review: #GEO-005-review-001
