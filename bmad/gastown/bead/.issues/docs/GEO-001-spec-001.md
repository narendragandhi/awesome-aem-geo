---
id: GEO-001-spec-001
workflow_id: GEO-001
type: specification
agent: docs
status: in_progress
priority: high
created: 2024-03-11T12:00:00Z
updated: 2024-03-11T12:00:00Z
depends_on: []
blocks: [GEO-001-impl-001, GEO-001-test-001]
---

# SEO Metadata Component Specification

## Overview

**Component/Feature**: SEO Metadata Component
**Type**: AEM Content Component
**Purpose**: Manages and renders SEO-critical metadata including title, description, canonical URLs, OpenGraph, and robots directives for AEM pages.

## Context

This component is the foundation for all SEO optimization in AEM. It provides:
- Title tag optimization (50-60 chars)
- Meta description (150-160 chars)
- Canonical URL management
- OpenGraph and Twitter Card support
- Robots meta directives
- Language/locale metadata

### Business Requirements

1. Authors can configure page-level SEO metadata via dialog
2. System automatically inherits parent page metadata when not overridden
3. Metadata renders in correct HTML `<head>` order
4. Supports both page-level and global defaults
5. Integrates with AEM's editable templates

### Technical Constraints

- AEM Version: AEMaaCS (Adobe Experience Manager as Cloud Service)
- Dependencies: AEM SDK API, Sling Models, HTL
- Compatible with: AEM 6.5+, AEMaaCS

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Title Tag | Page title with brand suffix | Required |
| Meta Description | Page description with character limits | Required |
| Canonical URL | Preferred URL for duplicate content | Required |
| OpenGraph Tags | Facebook/Social sharing | Required |
| Twitter Cards | Twitter sharing optimization | Required |
| Robots Meta | Index/follow directives | Required |
| Metadata Inheritance | Fallback to parent values | Required |
| Character Validation | Enforce length limits | Required |

### User Interactions

1. Author opens page properties dialog
2. Author fills SEO tab fields
3. Metadata renders in page `<head>`
4. Preview shows rendered metadata

### Data Model

```java
public interface SeoMetadataModel {
    // Page title (50-60 chars)
    String getTitle();
    
    // Meta description (150-160 chars)  
    String getDescription();
    
    // Canonical URL
    String getCanonicalUrl();
    
    // OpenGraph
    String getOgTitle();
    String getOgDescription();
    String getOgImage();
    String getOgType();
    
    // Twitter Cards
    String getTwitterCard();
    String getTwitterTitle();
    String getTwitterDescription();
    String getTwitterImage();
    
    // Robots
    String getRobots();
    
    // Locale
    String getLocale();
}
```

### Edge Cases

1. **Empty metadata** - Fall back to page title and auto-generated description
2. **Very long titles** - Truncate with ellipsis
3. **Missing canonical** - Auto-generate from page URL
4. **No OG image** - Use default site OG image
5. **Inheritance** - Use parent values when field is empty

## Non-Functional Requirements

### Performance

- Getters must be non-blocking
- No external API calls in getters
- Lazy initialization of all fields

### Security

- XSS protection on all text output
- URL validation for canonical URLs
- No hardcoded sensitive data

### Accessibility

- Proper meta tag rendering
- Alt text validation for OG images

## Acceptance Criteria

- [ ] Model provides all required metadata fields
- [ ] Title tag max 60 characters with ellipsis truncation
- [ ] Description max 160 characters with ellipsis truncation
- [ ] Canonical URL is valid absolute URL
- [ ] OpenGraph tags render correctly
- [ ] Twitter Card tags render correctly
- [ ] Robots meta respects noindex setting
- [ ] Inheritance works correctly from parent pages
- [ ] All tests pass
- [ ] 80%+ code coverage

## Technical Design

### File Structure

| File Type | Path |
|-----------|------|
| Sling Model | `core/src/main/java/com/awesomeaem/geo/models/SeoMetadataModel.java` |
| Implementation | `core/src/main/java/com/awesomeaem/geo/models/impl/SeoMetadataModelImpl.java` |
| HTL Template | `ui.apps/src/main/content/jcr_root/apps/awesome-aem-geo/components/seo-metadata/seo-metadata.html` |
| Dialog | `ui.apps/src/main/content/jcr_root/apps/awesome-aem-geo/components/seo-metadata/_cq_dialog/.content.xml` |
| Spec Test | `core/src/test/java/com/awesomeaem/geo/models/SeoMetadataSpecTest.java` |
| Unit Test | `core/src/test/java/com/awesomeaem/geo/models/SeoMetadataModelImplTest.java` |

### Dependencies

```xml
<dependency>
    <groupId>com.adobe.aem</groupId>
    <artifactId>aem-sdk-api</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.apache.sling</groupId>
    <artifactId>org.apache.sling.models.api</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>io.wcm</groupId>
    <artifactId>io.wcm.testing.aem-mock</artifactId>
    <scope>test</scope>
</dependency>
```

### Resource Type

`awesome-aem-geo/components/seo/seo-metadata`

### HTL Usage

```html
<sly data-sly-use.seo="com.awesomeaem.geo.models.SeoMetadataModel">
    <title data-sly-test="${seo.title}">${seo.title}</title>
    <meta name="description" content="${seo.description}">
    <link rel="canonical" href="${seo.canonicalUrl}">
    <meta property="og:title" content="${seo.ogTitle}">
    <meta property="og:description" content="${seo.ogDescription}">
    <meta property="og:image" content="${seo.ogImage}">
    <meta name="robots" content="${seo.robots}">
</sly>
```

## Progress Log

### 2024-03-11
Specification created following TDD methodology.

## Notes

- This is the foundational component for all SEO features
- Other components (JSON-LD, Sitemap) will depend on this
- Consider integration with AEM Tags for keyword management
