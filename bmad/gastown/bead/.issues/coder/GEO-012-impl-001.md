---
id: GEO-012-impl-001
workflow_id: GEO-012
type: implementation
agent: coder
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: [GEO-012-spec-001]
blocks: [GEO-012-test-001, GEO-012-review-001]
---

# Implement JSON-LD Schema Generation from Content

## Context

Implement the changes described in SPEC-GEO-012-spec-001.

**Component Type**: AEM Service
**Reference**: bmad/gastown/bead/.issues/docs/GEO-012-spec-001.md

## Acceptance Criteria

- [ ] No sample placeholder values
- [ ] Schema output matches content
- [ ] Validation prevents invalid schema
- [ ] Code compiles without errors

## Progress Log

### 2026-04-06T00:00:00Z
Issue created by Mayor during GEO hardening workflow.

### 2026-04-07T08:47:00-04:00
- Enforced required-field validation on generated JSON-LD output.
- Added tests to ensure empty output when required fields are missing.

## Files Changed

- core/src/main/java/com/awesomeaem/geo/services/impl/JsonLdSchemaServiceImpl.java
- core/src/test/java/com/awesomeaem/geo/services/JsonLdSchemaServiceImplTest.java
