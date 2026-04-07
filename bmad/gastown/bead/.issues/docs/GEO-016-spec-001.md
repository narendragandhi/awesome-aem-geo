---
id: GEO-016-spec-001
workflow_id: GEO-016
type: specification
agent: docs
status: pending
priority: high
created: 2026-04-06T00:00:00Z
updated: 2026-04-06T00:00:00Z
depends_on: []
blocks: [GEO-016-impl-001, GEO-016-test-001]
---

# Robots Rules + AI Bot Config Specification

## Overview

**Component/Feature**: Robots Rules + AI Bot Config
**Type**: AEM Service
**Purpose**: Improve robots.txt matching and centralize AI bot list in config

## Context

Current robots matching is naive and bot lists are duplicated across services.

### Business Requirements

1. Implement longest-match path evaluation
2. Centralize AI bot user agents in OSGi config
3. Remove unused caches

### Technical Constraints

- AEM Version: AEM as a Cloud Service / 6.5+
- Dependencies: aem-sdk-api, Sling Models, HTL
- Compatible with: Java 21

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Robots matching | Implement longest-match allow/disallow precedence | High |
| Bot list config | OSGi config shared across services | High |

### Edge Cases

1. Unknown bot -> wildcard rules
2. Empty config -> safe defaults

## Acceptance Criteria

- [ ] Robots evaluation follows precedence
- [ ] Bot list configurable and reused
- [ ] Tests cover matching rules
- [ ] Code compiles without errors
- [ ] All unit tests pass

## Progress Log

### 2026-04-06T00:00:00Z
Specification created by Mayor during GEO hardening workflow.
