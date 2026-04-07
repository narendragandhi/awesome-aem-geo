---
id: GEO-013-spec-001
workflow_id: GEO-013
type: specification
agent: docs
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: []
blocks: [GEO-013-impl-001, GEO-013-test-001]
---

# Image SEO Model + Image Schema Improvements Specification

## Overview

**Component/Feature**: Image SEO Model + Image Schema Improvements
**Type**: AEM Service + Sling Model
**Purpose**: Ensure Image SEO model and schema are generated from real DAM metadata

## Context

ImageSeoModel lacks service injection and ImageSeoService emits sample sitemap and generic data.

### Business Requirements

1. Inject ImageSeoService into model
2. Use DAM metadata for schema fields
3. Image sitemap should use actual assets

### Technical Constraints

- AEM Version: AEM as a Cloud Service / 6.5+
- Dependencies: aem-sdk-api, Sling Models, HTL
- Compatible with: Java 21

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| OSGi service injection | Use @OSGiService for ImageSeoService | Critical |
| DAM metadata mapping | Use asset metadata for schema fields | High |

### Edge Cases

1. Missing asset metadata -> omit fields
2. Invalid fileReference -> skip schema

## Acceptance Criteria

- [ ] Image schema populated from real metadata
- [ ] Image SEO model produces schema when service available
- [ ] No sample sitemap content
- [ ] Code compiles without errors
- [ ] All unit tests pass

## Progress Log

### 2026-04-06T00:00:00Z
Specification created by Mayor during GEO hardening workflow.
