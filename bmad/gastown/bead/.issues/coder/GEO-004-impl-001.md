---
id: GEO-004-impl-001
workflow_id: GEO-004
type: implementation
agent: coder
status: pending
priority: high
created: 2024-03-11T17:00:00Z
updated: 2024-03-11T17:00:00Z
depends_on: [GEO-004-spec-001]
blocks: [GEO-004-test-001, GEO-004-review-001]
---

# Implement AI Bot Handler Component

## Context

Implement the AI Bot Handler AEM component as specified in GEO-004-spec-001.

**Component Type**: OSGi Service + Filter
**Reference**: bmad/gastown/bead/.issues/docs/GEO-004-spec-001.md

## Specification Summary

AI Bot Handler detects AI crawler user agents (GPTBot, ClaudeBot, PerplexityBot, etc.) and provides optimized responses for LLM visibility. It includes:
- User agent detection service
- Request filter for AI bots
- Robots.txt directive handling
- Analytics tracking

## Acceptance Criteria

- [ ] AiBotHandlerService interface created
- [ ] AiBotHandlerServiceImpl implementation with all bot patterns
- [ ] AiBotFilter sling filter registered
- [ ] getExportedType() method implemented for Sling Model export
- [ ] Code compiles without errors
- [ ] Follows project coding standards

## Technical Details

### Service Interface Requirements

```java
public interface AiBotHandlerService {
    
    boolean isAiBot(HttpServletRequest request);
    
    String getBotName(HttpServletRequest request);
    
    boolean isAllowed(HttpServletRequest request);
    
    void recordVisit(String botName, String path, Instant timestamp);
    
    List<AiBotVisit> getRecentVisits(int limit);
}
```

### Supported Bot Patterns

| Bot | Pattern |
|-----|---------|
| GPTBot | `GPTBot` |
| ClaudeBot | `ClaudeBot` |
| Claude-Web | `Claude-Web` |
| Google-Extended | `Google-Extended` |
| PerplexityBot | `PerplexityBot` |
| Bytespider | `Bytespider` |
| Amazonbot | `Amazonbot` |
| OAI-SearchBot | `OAI-SearchBot` |
| Applebot | `Applebot` |
| DuckAssistBot | `DuckAssistBot` |
| ChatGPT-User | `ChatGPT-User` |

### File Locations

| File Type | Path |
|-----------|------|
| Service Interface | `core/src/main/java/com/awesomeaem/geo/services/AiBotHandlerService.java` |
| Service Implementation | `core/src/main/java/com/awesomeaem/geo/services/impl/AiBotHandlerServiceImpl.java` |
| Filter | `core/src/main/java/com/awesomeaem/geo/filters/AiBotFilter.java` |

## Progress Log

### 2024-03-11
Issue created during AI Bot Handler workflow.

## Handoff Notes

- Bot detection uses case-insensitive substring matching
- Filter should run after authentication (order ~700)
- Consider caching bot detection results per user agent string

## Files Changed

<!-- Updated as work progresses -->

## Related Issues

- Specification: #GEO-004-spec-001
- Testing: #GEO-004-test-001
- Review: #GEO-004-review-001
