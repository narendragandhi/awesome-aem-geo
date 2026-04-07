---
id: GEO-010-impl-001
workflow_id: GEO-010
type: implementation
agent: coder
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: [GEO-010-spec-001]
blocks: [GEO-010-test-001, GEO-010-review-001]
---

# Implement SEO Metadata Page Inheritance + Canonical Externalizer

## Context

Implement the changes described in SPEC-GEO-010-spec-001.

**Component Type**: AEM Sling Model
**Reference**: bmad/gastown/bead/.issues/docs/GEO-010-spec-001.md

## Acceptance Criteria

- [ ] Metadata values resolve from page/inheritance
- [ ] Canonical URLs are absolute and configurable
- [ ] Behavior covered by tests
- [ ] Code compiles without errors

## Progress Log

### 2026-04-06T00:00:00Z
Issue created by Mayor during GEO hardening workflow.

### 2026-04-06T00:32:00-04:00
- Updated SEO metadata model to resolve page properties via inheritance.
- Canonical URLs now use Externalizer when available and site domain config fallback.
- Updated unit tests to reflect page property resolution.

## Files Changed

- core/src/main/java/com/awesomeaem/geo/models/impl/SeoMetadataModelImpl.java
- core/src/test/java/com/awesomeaem/geo/models/impl/SeoMetadataModelImplTest.java
