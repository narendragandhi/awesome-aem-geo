---
id: GEO-005-spec-001
workflow_id: GEO-005
type: specification
agent: docs
status: in_progress
priority: high
created: 2024-03-11T17:30:00Z
updated: 2024-03-11T17:30:00Z
depends_on: []
blocks: [GEO-005-impl-001, GEO-005-test-001]
---

# Robots.txt Generator Component Specification

## Overview

**Component/Feature**: Robots.txt Generator
**Type**: AEM Servlet + Service
**Purpose**: Dynamically generate robots.txt with AI-specific directives for LLM crawling control.

## Context

AI bots respect robots.txt just like search engines. This component provides fine-grained control over which AI bots can crawl what content, including crawl-delay for rate limiting.

### Business Requirements

1. Generate valid robots.txt dynamically
2. Support AI-specific user-agent rules
3. Allow/disallow paths based on content type
4. Support crawl-delay for rate limiting
5. Cache generated output
6. Support sitemap reference

### Technical Constraints

- AEM Version: AEMaaCS (AEM 6.5+)
- Output: text/plain
- Served at: /robots.txt

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Dynamic Generation | Build robots.txt from AEM content | Required |
| AI Bot Rules | Specific rules for AI crawlers | Required |
| Path Filtering | Allow/disallow by content path | Required |
| Crawl Delay | Rate limiting support | Optional |
| Sitemap Reference | Auto-link to sitemap.xml | Required |

### AI Bot User Agents

```
Allow: /content/blog
Allow: /content/articles
Disallow: /etc
Disallow: /libs
Disallow: /bin
Crawl-delay: 1

# AI-specific
User-agent: GPTBot
Allow: /content/public
Disallow: /content/members

User-agent: ClaudeBot
Allow: /content/public
Disallow: /content/members
```

### User Interactions

1. Request to /robots.txt
2. Service builds robots.txt from configuration
3. Rules filtered by content path
4. Response served with proper headers

### Data Model

```java
public interface RobotsTxtService {
    
    String generateRobotsTxt(String domain);
    
    List<RobotsRule> getRules();
    
    boolean isPathAllowed(String userAgent, String path);
    
    int getCrawlDelay(String userAgent);
}
```

```java
public class RobotsRule {
    private String userAgent;
    private String directive;  // Allow or Disallow
    private String pathPattern;
    private int crawlDelay;
}
```

### Edge Cases

1. **No rules configured** - Return minimal robots.txt
2. **Invalid patterns** - Skip invalid rules
3. **Cache miss** - Generate fresh content
4. **Domain variations** - Handle multi-site

## Non-Functional Requirements

### Performance

- Cache robots.txt for 1 hour
- Generate in < 100ms
- CDN-compatible headers

### SEO/LLM Requirements

- Include sitemap reference
- Clear allow/disallow rules
- AI-specific directives

## Acceptance Criteria

- [ ] Valid robots.txt format
- [ ] AI bot rules work correctly
- [ ] Sitemap reference included
- [ ] All tests pass
- [ ] 80%+ code coverage

## Technical Design

### File Structure

| File Type | Path |
|-----------|------|
| Service Interface | `core/src/main/java/com/awesomeaem/geo/services/RobotsTxtService.java` |
| Service Implementation | `core/src/main/java/com/awesomeaem/geo/services/impl/RobotsTxtServiceImpl.java` |
| Servlet | `core/src/main/java/com/awesomeaem/geo/servlets/RobotsTxtServlet.java` |
| Config | `core/src/main/java/com/awesomeaem/geo/config/RobotsTxtConfig.java` |

## Progress Log

### 2024-03-11
Specification created.

## Notes

- Depends on Sitemap Generator (GEO-003)
- Integrates with AI Bot Handler (GEO-004)
