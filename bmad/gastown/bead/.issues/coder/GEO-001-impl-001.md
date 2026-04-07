---
id: GEO-001-impl-001
workflow_id: GEO-001
type: implementation
agent: coder
status: pending
priority: high
created: 2024-03-11T12:00:00Z
updated: 2024-03-11T12:00:00Z
depends_on: [GEO-001-spec-001]
blocks: [GEO-001-test-001, GEO-001-review-001]
---

# Implement SEO Metadata Component

## Context

Implement the SEO Metadata AEM component as specified in SPEC-GEO-001-spec-001.

**Reference**: bmad/gastown/bead/.issues/docs/GEO-001-spec-001.md

## Specification Summary

- **Component**: SEO Metadata
- **Type**: AEM Content Component
- **Purpose**: Manages SEO-critical metadata (title, description, canonical, OpenGraph, robots)

## Acceptance Criteria

- [ ] Sling Model interface created with all required methods
- [ ] Implementation class with proper annotations
- [ ] Model implements ComponentExporter for JSON
- [ ] HTL template renders all metadata correctly
- [ ] Dialog allows author configuration
- [ ] Code compiles without errors
- [ ] Follows project coding standards

## Technical Details

### Sling Model Interface

```java
@Model(adaptables = SlingHttpServletRequest.class, 
       adapters = {SeoMetadataModel.class, ComponentExporter.class})
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
          extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public interface SeoMetadataModel extends ComponentExporter {
    
    String RESOURCE_TYPE = "awesome-aem-geo/components/seo/seo-metadata";
    
    String getTitle();
    String getDescription();
    String getCanonicalUrl();
    String getOgTitle();
    String getOgDescription();
    String getOgImage();
    String getOgType();
    String getTwitterCard();
    String getTwitterTitle();
    String getTwitterDescription();
    String getTwitterImage();
    String getRobots();
    String getLocale();
}
```

### Implementation Requirements

1. **Title**: Max 60 chars, truncate with "..." if exceeded
2. **Description**: Max 160 chars, truncate with "..." if exceeded  
3. **Canonical**: Auto-generate from page path if not set
4. **OG**: Fall back to title/description if not set
5. **Robots**: Support index/noindex and follow/nofollow
6. **Inheritance**: Check parent page for fallback values

### File Locations

| File Type | Path |
|-----------|------|
| Interface | `core/src/main/java/com/awesomeaem/geo/models/SeoMetadataModel.java` |
| Implementation | `core/src/main/java/com/awesomeaem/geo/models/impl/SeoMetadataModelImpl.java` |
| HTL Template | `ui.apps/src/main/content/jcr_root/apps/awesome-aem-geo/components/seo/seo-metadata/seo-metadata.html` |
| Dialog | `ui.apps/src/main/content/jcr_root/apps/awesome-aem-geo/components/seo/seo-metadata/_cq_dialog/.content.xml` |

## Progress Log

### 2024-03-11
Issue created for implementation.

## Handoff Notes

<!-- For Tester: Document key files, business logic locations, and edge cases -->

## Files Changed

<!-- Updated as work progresses -->

## Related Issues

- Specification: #GEO-001-spec-001
- Testing: #GEO-001-test-001
- Review: #GEO-001-review-001
