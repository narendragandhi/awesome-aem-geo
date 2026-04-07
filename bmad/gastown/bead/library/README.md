# Awesome AEM GEO - Shared Context & Memory

## Project Overview

**Project**: awesome-aem-geo
**Domain**: AEM Search Engine Optimization & LLM Optimization
**Version**: 1.0.0-SNAPSHOT
**AEM Version**: AEM as Cloud Service (AEMaaCS)

## Architecture

### Core Components

1. **SEO Metadata** - Title, description, canonical URLs, OpenGraph
2. **JSON-LD Schema** - Schema.org markup generation
3. **Sitemap Generator** - XML sitemaps for search engines and AI
4. **AI Bot Handler** - Detection and optimized responses
5. **Content Quality Signals** - E-E-A-T markup

### Module Structure

```
awesome-aem-geo/
├── core/                    # OSGi services and Sling Models
│   └── src/main/java/
│       └── com/awesomeaem/geo/
│           ├── models/       # Sling Models
│           ├── services/    # OSGi Services
│           └── servlets/    # Servlets
├── ui.apps/                 # HTL templates and components
├── SPEC.md                  # Project specification
└── bmad/                    # GasTown configuration
```

## Key Decisions

### Decision 1: Spec-Driven Development
- All components start with SPEC.md
- TDD: Tests written before implementation
- BEAD methodology for task breakdown

### Decision 2: AEM Best Practices
- Use Core Components where possible
- Sling Models with ComponentExporter
- HTL for all rendering
- OSGi services for business logic

### Decision 3: Testing Strategy
- JUnit 5 for unit tests
- AEM Mocks for integration tests
- 80% minimum code coverage
- Spec tests to validate requirements

## Component Registry

| Component | Status | Issue |
|-----------|--------|-------|
| SEO Metadata | Planned | GEO-001 |
| JSON-LD Schema | Planned | GEO-002 |
| Sitemap Generator | Planned | GEO-003 |
| AI Bot Handler | Planned | GEO-004 |
| RSS Feed | Planned | GEO-005 |

## Dependencies

### Core Dependencies
- AEM SDK API (2024.1+)
- Apache Sling Models
- Jackson for JSON
- JUnit 5, Mockito
- wcm.io AEM Mocks

### External Integrations
- Schema.org (built-in)
- Google Rich Results Test
- AI Crawler User Agents

## Conventions

### Package Naming
- `com.awesomeaem.geo.models` - Sling Models
- `com.awesomeaem.geo.services` - OSGi Services
- `com.awesomeaem.geo.servlets` - Servlets

### Resource Types
- `awesome-aem-geo/components/content/seo-metadata`
- `awesome-aem-geo/components/content/json-ld`
- `awesome-aem-geo/components/structure/sitemap`

### Naming Conventions
- Models: `{ComponentName}Model.java`
- HTL: `{component-name}.html`
- Tests: `{ComponentName}ModelTest.java`, `{ComponentName}SpecTest.java`

## Progress

- [x] Project structure created
- [x] GasTown configuration
- [x] BEAD templates
- [ ] SPEC-GEO-001: SEO Metadata Component
- [ ] Implementation: SEO Metadata
- [ ] Tests: SEO Metadata
- [ ] Review: SEO Metadata

## Next Steps

1. Complete SEO Metadata component (GEO-001)
2. Move to JSON-LD Schema (GEO-002)
3. Continue with remaining components

---

Last Updated: 2024
