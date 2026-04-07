---
id: GEO-010-spec-001
workflow_id: GEO-010
type: specification
agent: docs
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: []
blocks: [GEO-010-impl-001, GEO-010-test-001]
---

# SEO Metadata Page Inheritance + Canonical Externalizer Specification

## Overview

**Component/Feature**: SEO Metadata Page Inheritance + Canonical Externalizer
**Type**: AEM Sling Model
**Purpose**: Use page properties with inheritance and generate canonical URLs via Externalizer/site config

## Context

SEO model currently reads component resource and uses hardcoded default domain.

### Business Requirements

1. Read metadata from current page properties with inheritance
2. Canonical URL generated via Externalizer and site config
3. Fallbacks maintained with truncation limits

### Technical Constraints

- AEM Version: AEM as a Cloud Service / 6.5+
- Dependencies: aem-sdk-api, Sling Models, HTL
- Compatible with: Java 21

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Page property resolution | Resolve metadata from Page and inherited properties | Critical |
| Canonical via Externalizer | Use Externalizer or configured domain rather than hardcoded value | High |

### Edge Cases

1. Missing title/description -> empty string
2. No Externalizer config -> safe fallback

## Acceptance Criteria

- [ ] Metadata values resolve from page/inheritance
- [ ] Canonical URLs are absolute and configurable
- [ ] Behavior covered by tests
- [ ] Code compiles without errors
- [ ] All unit tests pass

## Progress Log

### 2026-04-06T00:00:00Z
Specification created by Mayor during GEO hardening workflow.
