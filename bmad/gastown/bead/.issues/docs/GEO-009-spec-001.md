---
id: GEO-009-spec-001
workflow_id: GEO-009
type: specification
agent: docs
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: []
blocks: [GEO-009-impl-001, GEO-009-test-001]
---

# JSON-LD + HTL Wiring Fixes Specification

## Overview

**Component/Feature**: JSON-LD + HTL Wiring Fixes
**Type**: AEM HTL + Sling Model
**Purpose**: Ensure JSON-LD is rendered correctly and helper wiring works in HTL

## Context

Current HTL uses a helper class that is not a Sling Model and is never initialized; JSON-LD output in EEAT component is invalid.

### Business Requirements

1. JSON-LD helper must be a Sling Model or Use-API class that initializes from request
2. HTL must render valid JSON-LD and escape safely for script context
3. Remove or replace missing RequestHelper usage

### Technical Constraints

- AEM Version: AEM as a Cloud Service / 6.5+
- Dependencies: aem-sdk-api, Sling Models, HTL
- Compatible with: Java 21

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| JsonLdSchemaHelper as Sling Model | Expose hasSchema and jsonLd for HTL with proper init | Critical |
| EEAT JSON-LD output | Render valid JSON-LD script blocks using model/service output | High |

### Edge Cases

1. No schema returned -> no script output
2. Empty author/org data -> no invalid JSON

## Acceptance Criteria

- [ ] JSON-LD is rendered in head when schema is available
- [ ] EEAT component outputs valid JSON-LD
- [ ] No unresolved HTL references
- [ ] Code compiles without errors
- [ ] All unit tests pass

## Progress Log

### 2026-04-06T00:00:00Z
Specification created by Mayor during GEO hardening workflow.
