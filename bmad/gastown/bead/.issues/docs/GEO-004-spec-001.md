---
id: GEO-004-spec-001
workflow_id: GEO-004
type: specification
agent: docs
status: in_progress
priority: high
created: 2024-03-11T17:00:00Z
updated: 2024-03-11T17:00:00Z
depends_on: []
blocks: [GEO-004-impl-001, GEO-004-test-001]
---

# AI Bot Handler Component Specification

## Overview

**Component/Feature**: AI Bot Handler
**Type**: AEM OSGi Filter + Service
**Purpose**: Detect AI crawler user agents and provide optimized responses for LLM visibility and training data collection.

## Context

AI bots from OpenAI (GPTBot), Anthropic (ClaudeBot), Perplexity, Google (Google-Extended), and other AI companies are increasingly crawling the web to train models and provide AI-powered search. This component detects these bots and serves optimized content.

### Business Requirements

1. Detect AI crawler user agents accurately
2. Serve structured data optimized for AI consumption
3. Provide opt-out mechanisms for content owners
4. Track AI bot visits for analytics
5. Support robots.txt AI bot directives

### Technical Constraints

- AEM Version: AEMaaCS (AEM 6.5+)
- Filter Order: After authentication, before content
- Compatible with AEM Dispatcher

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| User Agent Detection | Identify AI bot crawlers | Required |
| Request Filtering | Block/allow based on rules | Required |
| Structured Response | JSON-LD for AI consumption | Required |
| Robots.txt Support | Honor AI bot directives | Required |
| Analytics | Track AI bot visits | Optional |
| Caching | Cache bot decisions | Required |

### Supported AI Bots

| Bot Name | User Agent Pattern |
|----------|-------------------|
| GPTBot (OpenAI) | `GPTBot` |
| ChatGPT-User (OpenAI) | `ChatGPT-User` |
| ClaudeBot (Anthropic) | `ClaudeBot` |
| Claude-Web (Anthropic) | `Claude-Web` |
| Google-Extended | `Google-Extended` |
| PerplexityBot | `PerplexityBot` |
| Bytespider | `Bytespider` |
| Amazonbot | `Amazonbot` |
| OAI-SearchBot | `OAI-SearchBot` |
| Applebot | `Applebot` |
| DuckAssistBot | `DuckAssistBot` |

### User Interactions

1. AI bot visits page
2. Filter detects AI bot user agent
3. Check robots.txt for opt-out
4. Optionally serve simplified HTML or structured data
5. Log visit for analytics

### Data Model

```java
public interface AiBotHandlerService {
    
    boolean isAiBot(HttpServletRequest request);
    
    String getBotName(HttpServletRequest request);
    
    boolean isAllowed(HttpServletRequest request);
    
    void recordVisit(String botName, String path, Instant timestamp);
    
    List<AiBotVisit> getRecentVisits(int limit);
}
```

```java
public interface AiBotVisit {
    String getBotName();
    String getPath();
    Instant getTimestamp();
    int getResponseStatus();
}
```

### Edge Cases

1. **Ambiguous user agents** - Default to not AI bot
2. **Blocked by robots.txt** - Return 403
3. **Rate limiting** - Allow but track
4. **Missing user agent** - Not an AI bot

## Non-Functional Requirements

### Performance

- User agent detection must be < 1ms
- Cache bot detection results
- No blocking I/O in filter chain

### Security

- Do not expose internal paths
- Validate all inputs
- No sensitive data in AI responses

### SEO Impact

- Must not affect human visitors
- Properly handle robots.txt
- Support canonical URLs

## Acceptance Criteria

- [ ] Detects all major AI bots by user agent
- [ ] Respects robots.txt directives
- [ ] Filter adds < 2ms latency
- [ ] All tests pass
- [ ] 80%+ code coverage
- [ ] Works with AEM Dispatcher

## Technical Design

### File Structure

| File Type | Path |
|-----------|------|
| Service Interface | `core/src/main/java/com/awesomeaem/geo/services/AiBotHandlerService.java` |
| Service Implementation | `core/src/main/java/com/awesomeaem/geo/services/impl/AiBotHandlerServiceImpl.java` |
| Filter | `core/src/main/java/com/awesomeaem/geo/filters/AiBotFilter.java` |
| Spec Test | `core/src/test/java/com/awesomeaem/geo/services/AiBotHandlerSpecTest.java` |
| Unit Test | `core/src/test/java/com/awesomeaem/geo/services/impl/AiBotHandlerServiceImplTest.java` |

### Dependencies

```xml
<dependency>
    <groupId>com.adobe.aem</groupId>
    <artifactId>aem-sdk-api</artifactId>
</dependency>
```

## Progress Log

### 2024-03-11
Specification created by Mayor during AI Bot Handler workflow.

## Notes

- Depends on SEO Metadata component (GEO-001) for structured data
- Consider integration with AEM Analytics
- May need CDN configuration for bot filtering
