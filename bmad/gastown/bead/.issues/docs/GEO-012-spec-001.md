---
id: GEO-012-spec-001
workflow_id: GEO-012
type: specification
agent: docs
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: []
blocks: [GEO-012-impl-001, GEO-012-test-001]
---

# JSON-LD Schema Generation from Content Specification

## Overview

**Component/Feature**: JSON-LD Schema Generation from Content
**Type**: AEM Service
**Purpose**: Replace placeholder JSON-LD with data from content properties

## Context

Schema service emits sample FAQ/HowTo/Product/Breadcrumb entries and hardcoded values.

### Business Requirements

1. Generate schema from actual properties or child resources
2. Validate required fields per schema type
3. Avoid placeholder values

### Technical Constraints

- AEM Version: AEM as a Cloud Service / 6.5+
- Dependencies: aem-sdk-api, Sling Models, HTL
- Compatible with: Java 21

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| FAQ/HowTo/Product/Breadcrumb real data | Build JSON-LD from structured content | High |
| Required field validation | Skip output when required fields missing | High |

### Edge Cases

1. Empty FAQ list -> no mainEntity
2. Invalid schemaType -> default to WebPage

## Acceptance Criteria

- [ ] No sample placeholder values
- [ ] Schema output matches content
- [ ] Validation prevents invalid schema
- [ ] Code compiles without errors
- [ ] All unit tests pass

## Progress Log

### 2026-04-06T00:00:00Z
Specification created by Mayor during GEO hardening workflow.
