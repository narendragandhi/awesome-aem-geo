---
id: GEO-002-spec-001
workflow_id: GEO-002
type: specification
agent: docs
status: in_progress
priority: high
created: 2024-03-11T14:00:00Z
updated: 2024-03-11T14:00:00Z
depends_on: []
blocks: [GEO-002-impl-001, GEO-002-test-001]
---

# JSON-LD Schema Component Specification

## Overview

**Component/Feature**: JSON-LD Schema Generator
**Type**: AEM OSGi Service + Component
**Purpose**: Generates Schema.org JSON-LD markup for AI search optimization and rich search results.

## Context

JSON-LD (JavaScript Object Notation for Linked Data) helps search engines and AI understand page content. This component generates proper Schema.org markup for various content types.

### Supported Schema Types

| Schema Type | Use Case |
|-------------|----------|
| Article | Blog posts, news articles |
| FAQPage | FAQ content pages |
| HowTo | Tutorial/step-by-step guides |
| Product | Product pages |
| Organization | Company information |
| BreadcrumbList | Navigation breadcrumbs |
| Person | Author profiles |
| WebPage | General web pages |

### Business Requirements

1. Support multiple Schema.org types
2. Generate valid JSON-LD output
3. Dynamic based on page properties
4. Integration with SEO Metadata component
5. Validate required fields per schema type

### Technical Constraints

- AEM Version: AEMaaCS (AEM 6.5+)
- Dependencies: AEM SDK, Gson
- Output: application/ld+json script tags

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| Schema Type Selection | Configure schema type via page properties | Required |
| Dynamic Field Mapping | Map AEM properties to Schema fields | Required |
| Nested Objects | Support nested Schema objects | Required |
| Context & Type | Proper @context and @type | Required |
| Validation | Check required fields | Required |

### User Interactions

1. Author selects schema type in page properties
2. Author fills schema-specific fields
3. System generates JSON-LD in page head

### Data Model

```java
public interface JsonLdSchemaService {
    
    String generateSchema(Page page, Resource content);
    
    boolean validateSchema(String schemaType, ValueMap properties);
    
    Set<String> getSupportedTypes();
}
```

### Schema-Specific Requirements

#### Article Schema
- @type: Article, NewsArticle, or BlogPosting
- Required: headline, author, datePublished
- Optional: image, dateModified, description

#### FAQPage Schema
- @type: FAQPage
- Required: mainEntity (array of Question)
- Question: acceptedAnswer, name

#### HowTo Schema
- @type: HowTo
- Required: name, step (array)
- Step: name, text, image

#### BreadcrumbList Schema
- @type: BreadcrumbList
- Required: itemListElement
- Each item: position, name, item (URL)

### Edge Cases

1. **Missing required fields** - Log warning, generate partial schema
2. **Invalid schema type** - Default to WebPage
3. **Empty content** - Return empty string
4. **Malformed data** - Skip invalid fields

## Non-Functional Requirements

### Performance
- Lazy generation (only when needed)
- No external API calls

### Security
- XSS-safe JSON output
- No sensitive data exposure

## Acceptance Criteria

- [ ] Supports all required schema types
- [ ] Generates valid JSON-LD
- [ ] Validates required fields
- [ ] Integrates with SEO Metadata
- [ ] All tests pass
- [ ] 80%+ code coverage

## Technical Design

### File Structure

| File Type | Path |
|-----------|------|
| Service Interface | `core/src/main/java/com/awesomeaem/geo/services/JsonLdSchemaService.java` |
| Service Implementation | `core/src/main/java/com/awesomeaem/geo/services/impl/JsonLdSchemaServiceImpl.java` |
| Schema Builders | `core/src/main/java/com/awesomeaem/geo/services/schemas/` |
| HTL Helper | `ui.apps/.../json-ld-schema.html` |
| Unit Test | `core/src/test/java/.../JsonLdSchemaServiceImplTest.java` |

### Dependencies

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
</dependency>
```

## Progress Log

### 2024-03-11
Specification created following TDD methodology.

## Notes

- Depends on SEO Metadata component (GEO-001)
- Consider AEM workflow for schema validation
- May need integration with Content Fragments
