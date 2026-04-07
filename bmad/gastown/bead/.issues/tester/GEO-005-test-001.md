---
id: GEO-005-test-001
workflow_id: GEO-005
type: testing
agent: tester
status: pending
priority: high
created: 2024-03-11T17:30:00Z
updated: 2024-03-11T17:30:00Z
depends_on: [GEO-005-impl-001]
blocks: [GEO-005-review-001]
---

# Test Robots.txt Generator Component

## Context

Write tests for the Robots.txt Generator as specified in GEO-005-spec-001.

**Reference**: 
- Specification: bmad/gastown/bead/.issues/docs/GEO-005-spec-001.md
- Implementation: bmad/gastown/bead/.issues/coder/GEO-005-impl-001.md

## Test Strategy

Follow TDD - write spec tests BEFORE implementation.

## Test Requirements

### Spec Tests Structure

```java
@DisplayName("RobotsTxtService")
class RobotsTxtSpecTest {
    
    @Nested
    @DisplayName("generateRobotsTxt")
    class GenerateRobotsTxt {
        // Tests for full robots.txt generation
    }
    
    @Nested
    @DisplayName("isPathAllowed")
    class IsPathAllowed {
        // Tests for path checking
    }
    
    @Nested
    @DisplayName("getCrawlDelay")
    class GetCrawlDelay {
        // Tests for crawl delay
    }
}
```

### Test Coverage

| Feature | Test Cases |
|---------|-----------|
| generateRobotsTxt | Valid format, AI rules included, sitemap reference |
| isPathAllowed | Allow public path, disallow internal, AI-specific |
| getCrawlDelay | Return delay for AI bots, 0 for others |

### Edge Cases

1. Empty rules - Return minimal robots.txt
2. Invalid patterns - Skip gracefully
3. No crawl-delay - Return 0

## Acceptance Criteria

- [ ] Spec tests written before implementation
- [ ] Unit tests for service implementation
- [ ] Tests use @DisplayName
- [ ] Tests pass
- [ ] 80% coverage

## Related Issues

- Specification: #GEO-005-spec-001
- Implementation: #GEO-005-impl-001
- Review: #GEO-005-review-001
