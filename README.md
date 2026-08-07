# Awesome AEM GEO

> A reference implementation for publishing trustworthy, machine-readable AEM content to search engines and AI agents.

## Overview

Awesome AEM GEO turns authored AEM content into a publishing contract that has:

- explicit Schema.org JSON-LD types and required fields;
- provenance, author, organization, and trust signals;
- safe structured exports for machine consumers; and
- crawl and discovery adapters such as robots.txt and sitemaps.

This is not a vector-search product, an AI gateway, or an authorization layer. The SEO and crawler features are delivery adapters around the content contract.

See [the architecture](./docs/architecture.md) for the boundaries and [the roadmap](./docs/roadmap.md) for the intentionally narrow next steps.

## How it fits

The page is the source of truth. This project maps authored AEM properties into a
small, testable content contract and exposes that contract through page metadata,
JSON-LD, a structured JSON export, robots.txt, and XML sitemaps. It does not
replace AEM authoring, search indexing, or AI inference.

## Project Structure

```
awesome-aem-geo/
├── bmad/                    # Historical planning and task artifacts
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

## Feature inventory

### Sling Models

| Component | Status | Description |
|-----------|--------|-------------|
| E-E-A-T Signals | Core | Experience, expertise, authoritativeness, trustworthiness, and provenance data |
| JSON-LD Schema | Core | Validated Article, FAQ, HowTo, Product, Organization, Breadcrumb, and WebPage output |
| Machine-readable Export | Core | Safe, structured content snapshot for machine consumers |
| Contract Validator | Core | Actionable completeness report for authored metadata and schema output |
| SEO Metadata | Adapter | Canonical, Open Graph, Twitter, and robots metadata |

### OSGi Services

| Service | Status | Description |
|---------|--------|-------------|
| Sitemap Generator | Adapter | XML sitemap generation from publishable AEM pages |
| Robots.txt Service | Adapter | Crawler rules and sitemap discovery reference |
| Image SEO Service | Adapter | ImageObject metadata and image sitemap support |
| AI Bot Handler | Observability | Advisory user-agent detection and visit recording; not access control |
| AI Analytics Service | Experimental | Bounded in-memory development telemetry; not production analytics |

### Filters

| Filter | Status | Description |
|--------|--------|-------------|
| AI Bot Filter | Observability | Records advisory bot visits; never authorizes requests |

### Public endpoints

| Endpoint | Description |
|----------|-------------|
| `/robots.txt` | Generated crawler rules and sitemap reference |
| `/sitemap.xml` | XML sitemap; accepts optional `root` and `max` query parameters |
| `/bin/awesome-aem-geo/content.json?path=/content/...` | Safe structured metadata snapshot for AI consumers |
| `/bin/awesome-aem-geo/contract.json?path=/content/...` | Contract validation report with errors, warnings, and emitted schema |
| `/bin/awesome-aem-geo/visibility.json` | Bounded crawler and AI-referral summary; not production analytics |

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- The AEM SDK version declared in `pom.xml`

### Build

```bash
# Build all modules without deploying
mvn clean verify

# Build specific modules
mvn clean verify -pl core,ui.apps,all -am
```

### Run Tests

```bash
# Run tests and coverage checks
mvn verify

# Deploy explicitly to a local author instance (credentials are not committed)
mvn install -PautoInstallSinglePackage -Daem.user=admin -Daem.password=admin
```

The install includes a demo page at `/content/awesome-aem-geo/us/en`.

### Showcase and verification

Open [the browser showcase](./showcase/index.html) after starting AEM. For a
repeatable endpoint check, run:

```bash
./scripts/verify-demo.sh
```

The verifier accepts `AEM_BASE_URL`, `AEM_USER`, `AEM_PASSWORD`, and
`AEM_DEMO_PATH` environment variables.

## Development workflow

Keep changes contract-first: define the authored input and emitted representation,
add a focused test, implement the mapping, and update the demo. New work should
strengthen the machine-readable publishing contract rather than add another
generic SEO feature. The `bmad/` directory contains historical planning artifacts.

## Documentation

- [Demo and showcase guide](./docs/demo.md)
- [Architecture and boundaries](./docs/architecture.md)
- [Roadmap](./docs/roadmap.md)
- [AI discovery readiness specification](./docs/specs/ai-discovery-readiness.md)
- [SEO and GEO positioning](./docs/seo-positioning.md)
- [Architecture decision records](./docs/decisions/)

## Dependencies

### Core
- AEM SDK API version declared in `pom.xml`
- Apache Sling Models
- Lombok
- GSON for JSON

### Testing
- JUnit 5
- Mockito
- AEM Mocks (wcm.io)

## License

MIT; see [LICENSE](./LICENSE).
