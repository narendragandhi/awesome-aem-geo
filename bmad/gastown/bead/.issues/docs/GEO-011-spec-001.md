---
id: GEO-011-spec-001
workflow_id: GEO-011
type: specification
agent: docs
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: []
blocks: [GEO-011-impl-001, GEO-011-test-001]
---

# Sitemap Generation from AEM Content Specification

## Overview

**Component/Feature**: Sitemap Generation from AEM Content
**Type**: AEM Service
**Purpose**: Generate sitemap from real AEM pages instead of samples

## Context

Sitemap service currently returns hardcoded sample URLs and uses non-threadsafe date formatter.

### Business Requirements

1. Traverse pages under rootPath and include eligible pages
2. Use thread-safe date formatting
3. Respect noindex and excluded paths

### Technical Constraints

- AEM Version: AEM as a Cloud Service / 6.5+
- Dependencies: aem-sdk-api, Sling Models, HTL
- Compatible with: Java 21

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Real page traversal | Use PageManager/QueryBuilder to collect pages | Critical |
| Thread-safe date formatting | Use DateTimeFormatter | High |

### Edge Cases

1. Large trees -> enforce maxUrls limit
2. Pages without lastModified -> omit or fallback

## Acceptance Criteria

- [ ] Sitemap includes only real pages
- [ ] No sample URLs remain
- [ ] Thread-safety ensured
- [ ] Code compiles without errors
- [ ] All unit tests pass

## Progress Log

### 2026-04-06T00:00:00Z
Specification created by Mayor during GEO hardening workflow.
