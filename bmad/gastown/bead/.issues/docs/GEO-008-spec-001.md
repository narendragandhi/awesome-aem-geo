---
id: GEO-008-spec-001
workflow_id: GEO-008
type: specification
agent: docs
status: in_progress
priority: high
created: 2024-03-11T18:30:00Z
updated: 2024-03-11T18:30:00Z
depends_on: [GEO-004, GEO-006]
blocks: [GEO-008-impl-001, GEO-008-test-001]
---

# AI Analytics Dashboard Specification

## Overview

**Component/Feature**: AI Analytics Dashboard
**Type**: AEM Service + Analytics Model
**Purpose**: Track, measure, and visualize LLM/AI bot traffic and content performance for AI optimization

## Context

As LLMs (ChatGPT, Claude, Perplexity, etc.) increasingly reference AEM-powered sites, it's critical to understand:
- Which AI bots are crawling the site
- What content they find most useful
- How the site performs in LLM responses
- SEO opportunities for AI visibility

### Business Requirements

1. Track AI bot visits with timing and behavior patterns
2. Record which pages/content are requested by AI crawlers
3. Provide analytics on AI referral traffic
4. Support dashboard visualization for content teams
5. Export data for integration with analytics platforms

### Technical Constraints

- AEM Version: AEM as a Cloud Service / 6.5+
- Dependencies: com.adobe.aem:aem-sdk-api, Apache Commons
- Storage: JCR nodes or external analytics (configurable)
- Compatible with: Java 21

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Bot Visit Tracking | Record AI bot visits with timestamp, user-agent, pages | Critical |
| Page Request Log | Log which pages AI bots request | Critical |
| Analytics Dashboard Data | Provide aggregated data for dashboard | High |
| Time-series Metrics | Track visits over time | High |
| Popular AI Content | Identify most crawled content by AI | Medium |
| Referral Reports | Generate AI referral reports | Medium |

### Data Model

```java
public interface AiAnalyticsService {
    
    record BotVisit(
        String botName,
        String userAgent,
        Instant timestamp,
        String requestedPath,
        int responseTimeMs,
        String statusCode
    ) {}
    
    record AnalyticsSummary(
        int totalVisits,
        Map<String, Integer> botBreakdown,
        Map<String, Integer> topPages,
        Instant firstVisit,
        Instant lastVisit
    ) {}
    
    void recordVisit(BotVisit visit);
    
    AnalyticsSummary getSummary(String timeRange);
    
    List<BotVisit> getRecentVisits(int limit);
    
    Map<String, Integer> getBotBreakdown();
    
    List<String> getTopPages(int limit);
}
```

### Edge Cases

1. High traffic - batch processing for large volumes
2. Storage limits - configurable retention period
3. Missing user agent - log as unknown
4. Response time outliers - cap at reasonable maximum

## Non-Functional Requirements

### Performance

- Async write for visit recording (non-blocking)
- In-memory caching for reads
- Target: <10ms for analytics queries

### Security

- No PII in analytics data
- Sanitize all paths before storage
- Role-based access for dashboard

### Scalability

- Support 100k+ visit records
- Configurable aggregation intervals
- Optional external analytics export

## Acceptance Criteria

- [ ] Record AI bot visits with all required fields
- [ ] Return accurate analytics summary
- [ ] Provide bot breakdown by type
- [ ] List top pages by AI requests
- [ ] Support time-range filtering
- [ ] Handle missing/null inputs gracefully
- [ ] Tests cover: happy path, edge cases
- [ ] Code compiles without errors
- [ ] All unit tests pass

## Technical Design

### File Structure

| File Type | Path |
|-----------|------|
| Service Interface | `core/src/main/java/.../services/AiAnalyticsService.java` |
| Service Impl | `core/src/main/java/.../services/impl/AiAnalyticsServiceImpl.java` |
| Spec Test | `core/src/test/java/.../services/AiAnalyticsSpecTest.java` |

## Progress Log

### 2024-03-11T18:30:00Z
Specification created for GEO-008 AI Analytics Dashboard.

## Notes

- Integrates with existing AiBotHandlerService
- Consider AEM Analytics or Adobe Launch integration
- Support GDPR-compliant data retention
