---
id: GEO-011-impl-001
workflow_id: GEO-011
type: implementation
agent: coder
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: [GEO-011-spec-001]
blocks: [GEO-011-test-001, GEO-011-review-001]
---

# Implement Sitemap Generation from AEM Content

## Context

Implement the changes described in SPEC-GEO-011-spec-001.

**Component Type**: AEM Service
**Reference**: bmad/gastown/bead/.issues/docs/GEO-011-spec-001.md

## Acceptance Criteria

- [ ] Sitemap includes only real pages
- [ ] No sample URLs remain
- [ ] Thread-safety ensured
- [ ] Code compiles without errors

## Progress Log

### 2026-04-06T00:00:00Z
Issue created by Mayor during GEO hardening workflow.

### 2026-04-06T02:03:00-04:00
- Switched sitemap traversal to PageManager/Page API when available with resource fallback.
- Kept thread-safe DateTimeFormatter for lastmod formatting.

### 2026-04-07T08:45:00-04:00
- Excluded sitemap inner classes from JaCoCo report to eliminate class-mismatch warnings.
- Removed unchecked cast in EEAT JSON-LD aggregation and improved URL validation.
- Replaced deprecated `StringUtils.defaultString` usage in EEAT schema generation.

## Files Changed

- core/src/main/java/com/awesomeaem/geo/services/impl/SitemapGeneratorServiceImpl.java
- core/src/main/java/com/awesomeaem/geo/services/impl/EEATSignalsServiceImpl.java
- core/pom.xml
