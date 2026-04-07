---
id: GEO-009-impl-001
workflow_id: GEO-009
type: implementation
agent: coder
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: [GEO-009-spec-001]
blocks: [GEO-009-test-001, GEO-009-review-001]
---

# Implement JSON-LD + HTL Wiring Fixes

## Context

Implement the changes described in SPEC-GEO-009-spec-001.

**Component Type**: AEM HTL + Sling Model
**Reference**: bmad/gastown/bead/.issues/docs/GEO-009-spec-001.md

## Acceptance Criteria

- [ ] JSON-LD is rendered in head when schema is available
- [ ] EEAT component outputs valid JSON-LD
- [ ] No unresolved HTL references
- [ ] Code compiles without errors

## Progress Log

### 2026-04-06T00:00:00Z
Issue created by Mayor during GEO hardening workflow.

### 2026-04-06T00:22:56-04:00
- Updated JSON-LD helper to adapt from request/resource and safely emit script-safe JSON.
- Fixed HTL wiring to use the helper interface and render valid JSON-LD in script tags.
- Tests: `mvn test -pl core -am -Dmaven.repo.local=/tmp/m2`

## Files Changed

- core/src/main/java/com/awesomeaem/geo/services/impl/JsonLdSchemaHelperImpl.java
- ui.apps/src/main/content/jcr_root/apps/awesome-aem-geo/components/seo/seo-metadata/seo-metadata.html
- ui.apps/src/main/content/jcr_root/apps/awesome-aem-geo/components/geo/eeat-signals/eeat-signals.html
