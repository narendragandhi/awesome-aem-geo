---
id: GEO-006-test-001
workflow_id: GEO-006
type: testing
agent: tester
status: pending
priority: high
created: 2024-03-11T17:45:00Z
updated: 2024-03-11T17:45:00Z
depends_on: [GEO-006-impl-001]
blocks: [GEO-006-review-001]
---

# Test AI Content Exporter Component

## Context

Write tests for the AI Content Exporter as specified in GEO-006-spec-001.

**Reference**: 
- Specification: bmad/gastown/bead/.issues/docs/GEO-006-spec-001.md
- Implementation: bmad/gastown/bead/.issues/coder/GEO-006-impl-001.md

## Test Strategy

Follow TDD - write spec tests BEFORE implementation.

## Test Requirements

### Spec Tests Structure

```java
@DisplayName("AiContentExporterModel")
class AiContentExporterSpecTest {
    
    @Nested
    @DisplayName("getTitle")
    class GetTitle {
        // Tests for title extraction
    }
    
    @Nested
    @DisplayName("getContent")
    class GetContent {
        // Tests for semantic content
    }
    
    @Nested
    @DisplayName("getSchema")
    class GetSchema {
        // Tests for schema.org data
    }
}
```

### Test Coverage

| Feature | Test Cases |
|---------|-----------|
| getTitle | Title from page properties |
| getDescription | Meta description |
| getUrl | Canonical URL |
| getAuthor | Author name and URL |
| getHeadings | H1, H2, H3 extracted |
| getParagraphs | Text content |
| getImages | Src, alt, caption |
| getSchema | JSON-LD data |

### Edge Cases

1. Missing title - Return empty string
2. No images - Return empty list
3. No author - Return null

## Acceptance Criteria

- [ ] Spec tests written before implementation
- [ ] Unit tests for model implementation
- [ ] Tests use @DisplayName
- [ ] Tests pass
- [ ] 80% coverage

## Related Issues

- Specification: #GEO-006-spec-001
- Implementation: #GEO-006-impl-001
- Review: #GEO-006-review-001
