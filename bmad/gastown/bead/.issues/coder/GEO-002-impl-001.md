---
id: GEO-002-impl-001
workflow_id: GEO-002
type: implementation
agent: coder
status: pending
priority: high
created: 2024-03-11T14:00:00Z
updated: 2024-03-11T14:00:00Z
depends_on: [GEO-002-spec-001]
blocks: [GEO-002-test-001, GEO-002-review-001]
---

# Implement JSON-LD Schema Component

## Context

Implement the JSON-LD Schema component as specified in SPEC-GEO-002-spec-001.

**Reference**: bmad/gastown/bead/.issues/docs/GEO-002-spec-001.md

## Specification Summary

- **Component**: JSON-LD Schema Generator
- **Type**: OSGi Service + HTL Helper
- **Purpose**: Generate Schema.org JSON-LD markup for AI search

## Acceptance Criteria

- [ ] JsonLdSchemaService interface created
- [ ] Implementation with all schema types
- [ ] JSON-LD generation works correctly
- [ ] Validation of required fields
- [ ] Code compiles without errors
- [ ] Follows project coding standards

## Technical Details

### Service Interface

```java
public interface JsonLdSchemaService {
    String generateSchema(Page page, Resource content);
    boolean validateSchema(String schemaType, ValueMap properties);
    Set<String> getSupportedTypes();
}
```

### Schema Types to Support

- Article (Article, NewsArticle, BlogPosting)
- FAQPage
- HowTo
- Product
- Organization
- BreadcrumbList
- Person
- WebPage

### File Locations

| File Type | Path |
|-----------|------|
| Service Interface | `core/src/main/java/com/awesomeaem/geo/services/JsonLdSchemaService.java` |
| Implementation | `core/src/main/java/com/awesomeaem/geo/services/impl/JsonLdSchemaServiceImpl.java` |
| HTL Helper | `ui.apps/.../json-ld-schema.html` |

## Progress Log

### 2024-03-11
Issue created for implementation.

## Related Issues

- Specification: #GEO-002-spec-001
- Testing: #GEO-002-test-001
- Review: #GEO-002-review-001
