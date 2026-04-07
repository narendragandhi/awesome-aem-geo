---
id: GEO-006-spec-001
workflow_id: GEO-006
type: specification
agent: docs
status: in_progress
priority: high
created: 2024-03-11T17:45:00Z
updated: 2024-03-11T17:45:00Z
depends_on: []
blocks: [GEO-006-impl-001, GEO-006-test-001]
---

# AI Content Exporter Component Specification

## Overview

**Component/Feature**: AI Content Exporter
**Type**: AEM Sling Model Exporter + Servlet
**Purpose**: Export AEM content in optimized JSON format for AI/LLM consumption without HTML overhead.

## Context

AI bots parsing HTML must strip tags, extract content, and handle JavaScript. This component provides a clean JSON API specifically designed for AI consumption, including structured data, metadata, and semantic content.

### Business Requirements

1. Export page content as clean JSON
2. Include all metadata (SEO, schema.org)
3. Semantic content extraction (headings, paragraphs, lists)
4. Image alt text and captions
5. Author and date information
6. Related content links

### Technical Constraints

- AEM Version: AEMaaCS (AEM 6.5+)
- Output: application/json
- Endpoints: /api/content/{path}.json

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| JSON Export | Clean JSON without HTML | Required |
| Metadata Include | SEO + schema.org data | Required |
| Content Extraction | Semantic HTML to JSON | Required |
| Image Optimization | Alt text, captions | Required |
| Related Content | Links to related pages | Optional |
| Caching | CDN cache support | Required |

### JSON Output Format

```json
{
  "title": "Page Title",
  "description": "Page description",
  "url": "https://example.com/content/page",
  "publishedDate": "2024-01-15",
  "modifiedDate": "2024-01-20",
  "author": {
    "name": "Author Name",
    "url": "https://example.com/authors/author"
  },
  "content": {
    "headings": [
      {"level": 1, "text": "Main Heading"},
      {"level": 2, "text": "Section Heading"}
    ],
    "paragraphs": [
      "First paragraph text...",
      "Second paragraph..."
    ],
    "lists": [
      {"type": "ul", "items": ["Item 1", "Item 2"]}
    ]
  },
  "images": [
    {
      "url": "https://example.com/content/image.jpg",
      "alt": "Image description",
      "caption": "Optional caption"
    }
  ],
  "links": [
    {"text": "Link Text", "url": "https://example.com/target"}
  ],
  "schema": {
    "@context": "https://schema.org",
    "@type": "Article",
    "headline": "Article Headline"
  }
}
```

### User Interactions

1. AI bot requests /api/content/page.json
2. Sling Model Exporter returns JSON
3. Content filtered for AI consumption
4. Response cached

### Data Model

```java
public interface AiContentExporterModel {
    
    String getTitle();
    
    String getDescription();
    
    String getUrl();
    
    Instant getPublishedDate();
    
    Instant getModifiedDate();
    
    AuthorInfo getAuthor();
    
    List<Heading> getHeadings();
    
    List<String> getParagraphs();
    
    List<ImageInfo> getImages();
    
    List<LinkInfo> getLinks();
    
    Map<String, Object> getSchema();
    
    String getExportedType();
}
```

### Edge Cases

1. **Missing metadata** - Use sensible defaults
2. **No images** - Return empty array
3. **Rich text only** - Extract plain text
4. **Large content** - Paginate or truncate

## Non-Functional Requirements

### Performance

- < 200ms response time
- CDN cacheable (public)
- ETags for caching

### LLM Optimization

- No HTML parsing needed
- Clear semantic structure
- Include all relevant context
- JSON-LD inline

## Acceptance Criteria

- [ ] Valid JSON output
- [ ] All metadata included
- [ ] Semantic content extracted
- [ ] Works with Sling Model Exporter
- [ ] All tests pass
- [ ] 80%+ code coverage

## Technical Design

### File Structure

| File Type | Path |
|-----------|------|
| Model Interface | `core/src/main/java/com/awesomeaem/geo/models/AiContentExporterModel.java` |
| Model Implementation | `core/src/main/java/com/awesomeaem/geo/models/impl/AiContentExporterModelImpl.java` |
| Exporter | Uses Sling Model Exporter |
| Test | `core/src/test/java/.../AiContentExporterSpecTest.java` |

## Progress Log

### 2024-03-11
Specification created.

## Notes

- Builds on SEO Metadata (GEO-001) and JSON-LD (GEO-002)
- Can replace full HTML parsing for AI bots
