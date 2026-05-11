# Awesome AEM GEO

> AEM GEO - Search Engine Optimization & LLM Optimization for Adobe Experience Manager

## Overview

Awesome AEM GEO is a collection of AEM components and tools for optimizing AEM-powered sites for:

- **Traditional Search** - Google, Bing, Yahoo
- **LLM Search** - ChatGPT, Perplexity, Claude, Gemini
- **AI-powered Search** - Semantic search, vector search

## Architecture

This project follows the **BMAD** (Business Model for AI Development) methodology with:

- **Spec-Driven Development** - All components start with SPEC.md
- **TDD** - Tests written before implementation
- **BEAD** - Breaking tasks into beads for agent-based development
- **Gastown** - Orchestrator for multi-agent workflows

## Project Structure

```
awesome-aem-geo/
├── bmad/                    # GasTown configuration
│   ├── gastown/
│   │   ├── agents/          # Agent personas
│   │   ├── workflows/      # Workflow definitions
│   │   ├── bead/           # BEAD issues and templates
│   │   │   ├── templates/  # Issue templates
│   │   │   ├── library/    # Shared context/memory
│   │   │   └── .issues/    # Active issues
│   │   └── config/         # Configuration files
├── core/                    # Core bundle (Java/OSGi)
│   └── src/
│       ├── main/java/      # Sling Models, Services
│       └── test/java/      # Unit tests
├── ui.apps/                # Content package
│   └── src/main/content/
│       └── jcr_root/      # HTL templates, components
├── all/                    # All-in-one package
└── pom.xml                 # Maven parent POM
```

## Components

### Sling Models

| Component | Status | Description |
|-----------|--------|-------------|
| SEO Metadata | Implemented | Title, description, canonical, OpenGraph, Twitter Cards, robots directives |
| E-E-A-T Signals | Implemented | Experience, Expertise, Authoritativeness, Trustworthiness signals |
| AI Content Exporter | Implemented | Structured JSON export for AI crawlers |
| Image SEO | Implemented | Image metadata, alt text validation, ImageObject schema |

### OSGi Services

| Service | Status | Description |
|---------|--------|-------------|
| JSON-LD Schema Service | Implemented | Schema.org markup generation (Article, FAQ, HowTo, Breadcrumb, etc.) |
| Sitemap Generator | Implemented | XML sitemaps with sitemap index support |
| AI Bot Handler | Implemented | Detection and visit recording for ClaudeBot, GPTBot, Perplexity |
| Robots.txt Service | Implemented | Dynamic robots.txt with crawl-delay and path rules |
| Image SEO Service | Implemented | ImageObject schema, alt text validation, image sitemap entries |
| EEAT Signals Service | Implemented | Author, Organization, Review, FactCheck schema |
| AI Analytics Service | Implemented | Track AI bot visits, analytics summary |

### Filters

| Filter | Status | Description |
|--------|--------|-------------|
| AI Bot Filter | Implemented | Servlet filter to detect and record AI bot visits |

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- AEM SDK 2026.2+

### Build

```bash
# Build all modules
mvn clean install

# Build specific module
mvn clean install -pl core
mvn clean install -pl ui.apps
```

### Run Tests

```bash
# Run all tests with coverage
mvn test

# Run with coverage report
mvn verify
```

## Development Workflow

1. **Create Issue** - Use BEAD template to create a new task
2. **Write SPEC** - Document requirements in SPEC-{issue}.md
3. **Write Tests** - TDD: Write failing tests first
4. **Implement** - Write code to pass tests
5. **Review** - Code review by reviewer agent
6. **Complete** - Move to completed folder

## Using Gastown

```bash
# Start a new workflow
# (Requires Claude Code or similar AI assistant)
```

See `bmad/gastown/config/gastown.yaml` for workflow configurations.

## Documentation

- [BMAD Methodology](./bmad/README.md)
- [GasTown Configuration](./bmad/gastown/config/gastown.yaml)
- [BEAD Templates](./bmad/gastown/bead/templates/)
- [Shared Context](./bmad/gastown/bead/library/README.md)

## Dependencies

### Core
- AEM SDK API 2026.2+
- Apache Sling Models
- Lombok
- GSON for JSON

### Testing
- JUnit 5
- Mockito
- AEM Mocks (wcm.io)

## License

MIT
