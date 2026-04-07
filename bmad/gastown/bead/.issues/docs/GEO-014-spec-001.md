---
id: GEO-014-spec-001
workflow_id: GEO-014
type: specification
agent: docs
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: []
blocks: [GEO-014-impl-001, GEO-014-test-001]
---

# AI Content Exporter Author Model Fix Specification

## Overview

**Component/Feature**: AI Content Exporter Author Model Fix
**Type**: AEM Sling Model
**Purpose**: Use a correct author model instead of AiBotVisit

## Context

AiContentExporterModel currently returns AiBotVisit as author, which is incorrect.

### Business Requirements

1. Define a dedicated Author record/type
2. Export author data correctly
3. Maintain JSON export compatibility

### Technical Constraints

- AEM Version: AEM as a Cloud Service / 6.5+
- Dependencies: aem-sdk-api, Sling Models, HTL
- Compatible with: Java 21

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Author record | Replace AiBotVisit usage with AuthorInfo record | High |

### Edge Cases

1. Missing author -> null
2. Author URL optional

## Acceptance Criteria

- [ ] Author data uses correct type
- [ ] No AiBotVisit leakage in exporter
- [ ] Tests updated
- [ ] Code compiles without errors
- [ ] All unit tests pass

## Progress Log

### 2026-04-06T00:00:00Z
Specification created by Mayor during GEO hardening workflow.
