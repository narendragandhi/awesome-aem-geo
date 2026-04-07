---
id: GEO-003-spec-001
workflow_id: GEO-003
type: specification
agent: docs
status: in_progress
priority: high
created: 2024-03-11T16:00:00Z
updated: 2024-03-11T16:00:00Z
depends_on: []
blocks: [GEO-003-impl-001, GEO-003-test-001]
---

# Sitemap Generator Component Specification

## Overview

**Component/Feature**: XML Sitemap Generator
**Type**: AEM OSGi Service + Servlet
**Purpose**: Generate XML sitemaps for search engines and AI crawlers to discover content.

## Context

XML sitemaps help search engines and AI crawlers discover and index content efficiently. This component generates compliant XML sitemaps with support for multilingual content.

### Business Requirements

1. Generate valid XML sitemap protocol (sitemap.org)
2. Support sitemap indexes for large sites
3. Exclude noindex pages
4. Support multilingual sitemaps
5. Respect lastmod dates
6. AI crawler-friendly output

### Technical Constraints

- AEM Version: AEMaaCS (AEM 6.5+)
- Output: XML (application/xml)
- Max URLs per sitemap: 50,000
- Max sitemaps per index: 1,000

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| XML Generation | Valid sitemap XML output | Required |
| Sitemap Index | Support multiple sitemaps | Required |
| Noindex Exclusion | Skip pages marked noindex | Required |
| Lastmod Support | Include modification dates | Required |
| Priority Support | Configurable priority per page | Optional |
| Changefreq | Change frequency hints | Optional |
| Multilingual | Language/region sitemaps | Optional |

### User Interactions

1. System generates sitemap on request
2. Sitemap accessible at /sitemap.xml
3. Sitemap index at /sitemap-index.xml

### Data Model

```java
public interface SitemapGeneratorService {
    
    String generateSitemap(String rootPath, int maxUrls);
    
    String generateSitemapIndex(List<String> sitemapUrls);
    
    boolean shouldIncludePage(Page page);
    
    SitemapEntry getEntryForPage(Page page);
}
```

### Sitemap Entry Properties

| Property | Description |
|----------|-------------|
| loc | URL of the page |
| lastmod | Last modification date |
| changefreq | Change frequency (always, hourly, daily, weekly, monthly, yearly, never) |
| priority | Priority 0.0-1.0 |

### Edge Cases

1. **Page marked noindex** - Skip from sitemap
2. **Too many pages** - Generate sitemap index
3. **Missing lastmod** - Use page creation date
4. **Redirect pages** - Include with appropriate priority

## Non-Functional Requirements

### Performance
- Cache generated sitemaps
- Incremental updates for large sites
- Background generation

### SEO/AI Requirements
- Include AI crawler-friendly metadata
- Support for sitemap extensions
- Clean, minimal XML

## Acceptance Criteria

- [ ] Generates valid XML sitemap
- [ ] Supports sitemap index
- [ ] Excludes noindex pages
- [ ] Includes lastmod dates
- [ ] All tests pass
- [ ] 80%+ code coverage

## Technical Design

### File Structure

| File Type | Path |
|-----------|------|
| Service Interface | `core/src/main/java/com/awesomeaem/geo/services/SitemapGeneratorService.java` |
| Service Implementation | `core/src/main/java/com/awesomeaem/geo/services/impl/SitemapGeneratorServiceImpl.java` |
| Servlet | `core/src/main/java/com/awesomeaem/geo/servlets/SitemapServlet.java` |
| Index Servlet | `core/src/main/java/com/awesomeaem/geo/servlets/SitemapIndexServlet.java` |
| Unit Test | `core/src/test/java/.../SitemapGeneratorServiceImplTest.java` |

## Progress Log

### 2024-03-11
Specification created.

## Notes

- Depends on SEO Metadata component (GEO-001)
- Consider integration with AEM replication
- May need CDN cache invalidation
