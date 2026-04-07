---
id: GEO-001-test-001
workflow_id: GEO-001
type: testing
agent: tester
status: pending
priority: high
created: 2024-03-11T12:00:00Z
updated: 2024-03-11T12:00:00Z
depends_on: [GEO-001-spec-001, GEO-001-impl-001]
blocks: [GEO-001-review-001]
---

# Test SEO Metadata Component

## Context

Write tests for SEO Metadata AEM component as specified in SPEC-GEO-001-spec-001.

**Implementation Reference**: bmad/gastown/bead/.issues/coder/GEO-001-impl-001.md

## Test Requirements

### Unit Tests (JUnit 5 + Mockito)

- [ ] Test title truncation at 60 characters
- [ ] Test description truncation at 160 characters
- [ ] Test canonical URL generation from page path
- [ ] Test OG fallback to title/description
- [ ] Test robots directives (index/noindex, follow/nofollow)
- [ ] Test null handling for all fields
- [ ] Test inheritance from parent page
- [ ] Test default OG image when none set
- [ ] Test locale generation

### Spec Test (Behavior-Driven)

- [ ] Test full metadata rendering flow
- [ ] Test character limit enforcement
- [ ] Test URL validation

### Test Coverage Target

- Minimum 80% code coverage

### Test Framework

- JUnit 5
- Mockito for mocking
- AEM Mocks (wcm.io) for AEM context

## Technical Details

### Test Class Locations

| Test Type | Path |
|-----------|------|
| Spec Test | `core/src/test/java/com/awesomeaem/geo/models/SeoMetadataSpecTest.java` |
| Unit Test | `core/src/test/java/com/awesomeaem/geo/models/impl/SeoMetadataModelImplTest.java` |

### Test Data

```java
// Test constants
private static final int TITLE_MAX_LENGTH = 60;
private static final int DESC_MAX_LENGTH = 160;
private static final String DEFAULT_OG_IMAGE = "/content/dam/awesome-aem-geo/default-og.png";
```

## Acceptance Criteria

- [ ] All tests pass
- [ ] 80%+ code coverage
- [ ] Tests follow TDD approach (written before implementation logic added)
- [ ] Tests are maintainable and readable

## Progress Log

### 2024-03-11
Issue created for testing.

## Test Results

<!-- Updated as tests are written and executed -->

## Related Issues

- Specification: #GEO-001-spec-001
- Implementation: #GEO-001-impl-001
- Review: #GEO-001-review-001
