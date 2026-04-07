---
id: GEO-006-impl-001
workflow_id: GEO-006
type: implementation
agent: coder
status: pending
priority: high
created: 2024-03-11T17:45:00Z
updated: 2024-03-11T17:45:00Z
depends_on: [GEO-006-spec-001]
blocks: [GEO-006-test-001, GEO-006-review-001]
---

# Implement AI Content Exporter Component

## Context

Implement the AI Content Exporter as specified in GEO-006-spec-001.

**Component Type**: Sling Model + Exporter
**Reference**: bmad/gastown/bead/.issues/docs/GEO-006-spec-001.md

## Specification Summary

Export AEM page content as clean JSON for AI/LLM consumption. Includes:
- Title, description, URL
- Author and date info
- Semantic content (headings, paragraphs, lists)
- Images with alt text
- Schema.org data

## Acceptance Criteria

- [ ] AiContentExporterModel interface created
- [ ] Model implements ComponentExporter
- [ ] getExportedType() returns correct type
- [ ] JSON includes all semantic content
- [ ] Code compiles without errors

## Technical Details

### Model Interface

```java
public interface AiContentExporterModel extends ComponentExporter {
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
}
```

### JSON Output Example

```json
{
  "title": "Article Title",
  "description": "Description",
  "url": "https://site.com/content/page",
  "author": {"name": "Author", "url": "..."},
  "content": {
    "headings": [{"level": 1, "text": "..."}],
    "paragraphs": ["..."]
  },
  "images": [{"url": "...", "alt": "..."}],
  "schema": {"@type": "Article"}
}
```

### File Locations

| File Type | Path |
|-----------|------|
| Model Interface | `core/src/main/java/com/awesomeaem/geo/models/AiContentExporterModel.java` |
| Model Impl | `core/src/main/java/com/awesomeaem/geo/models/impl/AiContentExporterModelImpl.java` |

## Progress Log

### 2024-03-11
Issue created.

## Handoff Notes

- Use Sling Model Exporter for automatic JSON
- Resource type: `awesome-aem-geo/components/structure/ai-content-exporter`
- Access via: /content/page.model.json

## Related Issues

- Specification: #GEO-006-spec-001
- Testing: #GEO-006-test-001
- Review: #GEO-006-review-001
