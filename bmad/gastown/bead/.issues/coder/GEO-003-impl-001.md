---
id: GEO-003-impl-001
workflow_id: GEO-003
type: implementation
agent: coder
status: pending
priority: high
created: 2024-03-11T16:00:00Z
updated: 2024-03-11T16:00:00Z
depends_on: [GEO-003-spec-001]
blocks: [GEO-003-test-001, GEO-003-review-001]
---

# Implement Sitemap Generator Component

## Context

Implement the Sitemap Generator as specified in SPEC-GEO-003-spec-001.

**Reference**: bmad/gastown/bead/.issues/docs/GEO-003-spec-001.md

## Specification Summary

- **Component**: XML Sitemap Generator
- **Type**: OSGi Service + Servlet
- **Purpose**: Generate XML sitemaps for search engines and AI crawlers

## Acceptance Criteria

- [ ] SitemapGeneratorService interface created
- [ ] Implementation generates valid XML
- [ ] SitemapIndexServlet works
- [ ] Noindex pages excluded
- [ ] Code compiles without errors

## Technical Details

### Service Interface

```java
public interface SitemapGeneratorService {
    String generateSitemap(String rootPath, int maxUrls);
    String generateSitemapIndex(List<String> sitemapUrls);
    boolean shouldIncludePage(Page page);
}
```

### Sitemap Format

```xml
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>https://example.com/page.html</loc>
    <lastmod>2024-03-11</lastmod>
    <changefreq>weekly</changefreq>
    <priority>0.8</priority>
  </url>
</urlset>
```

### File Locations

| File Type | Path |
|-----------|------|
| Service Interface | `core/src/main/java/com/awesomeaem/geo/services/SitemapGeneratorService.java` |
| Implementation | `core/src/main/java/com/awesomeaem/geo/services/impl/SitemapGeneratorServiceImpl.java` |
| Servlet | `core/src/main/java/com/awesomeaem/geo/servlets/SitemapServlet.java` |

## Progress Log

### 2024-03-11
Issue created for implementation.

## Related Issues

- Specification: #GEO-003-spec-001
- Testing: #GEO-003-test-001
- Review: #GEO-003-review-001
