---
id: GEO-015-spec-001
workflow_id: GEO-015
type: specification
agent: docs
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: []
blocks: [GEO-015-impl-001, GEO-015-test-001]
---

# EEAT Signals Model Injection Fixes Specification

## Overview

**Component/Feature**: EEAT Signals Model Injection Fixes
**Type**: AEM Sling Model
**Purpose**: Fix multifield and date injection for EEAT signals model

## Context

Model uses @ChildResource List<String> and Instant injection which will not work with typical AEM dialog structures.

### Business Requirements

1. Use child resources or ValueMap for multifields correctly
2. Use Calendar/Date for date fields and convert to Instant
3. Avoid null pointer issues

### Technical Constraints

- AEM Version: AEM as a Cloud Service / 6.5+
- Dependencies: aem-sdk-api, Sling Models, HTL
- Compatible with: Java 21

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Multifield parsing | Handle authorCredentials/certifications/trustBadges | High |
| Date conversion | Convert Calendar/Date to Instant | High |

### Edge Cases

1. Empty multifields -> empty list
2. Missing dates -> null

## Acceptance Criteria

- [ ] Multifields inject correctly
- [ ] Dates are parsed safely
- [ ] Model returns stable defaults
- [ ] Code compiles without errors
- [ ] All unit tests pass

## Progress Log

### 2026-04-06T00:00:00Z
Specification created by Mayor during GEO hardening workflow.
