# SEO and GEO Positioning

Status: Maintainer guidance
Last reviewed: 2026-08-06

## Position

Awesome AEM GEO is a technical SEO and AI-discovery foundation for AEM. It
turns authored content into validated metadata, structured data, crawl
policies, machine-readable exports, and bounded diagnostic telemetry.

It is not a ranking service, a citation guarantee, an AI content generator, or
a replacement for Search Console, analytics, editorial review, or digital PR.

## Relationship to AEM Cloud SEO and URL Management

This project complements Adobe’s platform guidance; it does not replace it.
Adobe’s [SEO and URL Management best practices for AEM as a Cloud Service](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/overview/seo-and-url-management)
was last updated June 17, 2026 and covers a broader implementation surface.

| Adobe concern | Repository position |
|---|---|
| Page title, description, canonical, social metadata | Covered by the SEO Metadata component and tests |
| Robots policy and XML sitemap | Covered with project adapters and endpoint tests; AEM/Sling native sitemap behavior remains a deployment choice |
| Structured data and visible-content consistency | Covered by JSON-LD services and contract validation |
| AI crawler identification and referral diagnostics | Complementary project capability |
| Human-readable page names and URL hierarchy | Content-authoring and AEM project responsibility |
| Sling selectors, resource resolver mappings, vanity URLs, and aliases | Not implemented; use AEM/Sling configuration |
| Dispatcher/CDN rewrites, lowercase URLs, HTTPS, and cache invalidation | Not implemented; use Dispatcher/CDN and Cloud Manager configuration |
| 301 redirect strategy and legacy URL migration | Not implemented; use the deployment’s redirect layer |
| Sitemap scheduling, publish-tier externalization, and production monitoring | Partially covered by the project’s generator; production scheduling and externalization remain AEM configuration |

The practical conclusion is that an AEM SEO specialist should use this repo as
a content-contract, structured-data, AI-discovery, and validation layer inside
the wider AEM SEO architecture. It should not duplicate AEM’s URL-management
and delivery infrastructure.

## AEM.live alignment

The [AEM.live SEO & GEO guidance](https://www.aem.live/docs/seo-geo) reinforces
an important implementation choice: canonical page content and key metadata
should be available in the initial HTML response. LLM agents commonly consume
the response without rendering a browser, so this repository now includes
`scripts/verify-rendered-page.sh` to smoke-test title, canonical, description,
robots, and JSON-LD signals in the first payload.

The repository does not add server-side header/footer or fragment inlining as a
blanket optimization. AEM.live reports no measurable SEO upside from its
header/footer experiment and notes the potential performance cost. Any such
change belongs in a measured production experiment using Search Console and
Core Web Vitals, not in a default component feature.

## What SEO specialists should recognize

The project is strongest where it operationalizes established fundamentals:

- AEM remains the source of truth.
- Canonical content is delivered in machine-readable form.
- Structured data is checked against authored and visible content.
- Crawlability, sitemap discovery, and preview controls are explicit.
- Errors and warnings are separated so authors can act on them.
- Crawler activity and referral activity are measured separately.
- The demo and tests make the behavior reproducible.

This is deliberately “SEO plus discovery observability,” not a new ranking
discipline with secret markup.

## Evidence boundary

Google’s guidance says AI features use the same underlying SEO fundamentals and
that structured data should describe visible content. Adobe’s AEM guidance
likewise emphasizes server-rendered canonical content, structured data, and
performance. Practitioner surveys commonly identify structured data and AI
visibility measurement as active workstreams, but terminology such as GEO and
AEO is not standardized.

Sources reviewed:

- [Google: General Structured Data Guidelines](https://developers.google.com/search/docs/appearance/structured-data/sd-policies)
- [Google: AI features and your website](https://developers.google.com/search/docs/appearance/ai-features)
- [Adobe: SEO & GEO Best Practices for AEM](https://www.aem.live/docs/seo-geo)
- [Adobe: Make your AEM content visible to AI](https://experienceleague.adobe.com/en/perspectives/make-your-aem-content-visible-to-ai-a-practitioner-guide)
- [Search Engine Land: AI search optimization survey](https://searchengineland.com/ai-search-optimization-survey-2025-461939)

## Claims we can make

- The project makes AEM content more consistent and inspectable for search and
  machine consumers.
- It provides useful technical controls for crawl discovery and search
  previews.
- It detects selected content-contract and structured-data inconsistencies.
- It provides development telemetry that distinguishes crawler visits from
  ChatGPT referral signals.
- It is a useful starting point for an AEM team building a broader SEO and
  search-everywhere operating model.

## Claims we must not make

- “This guarantees Google rankings or AI citations.”
- “This makes content preferred by ChatGPT, Claude, or Google.”
- “JSON-LD alone makes a page authoritative.”
- “A crawler visit proves that a page was indexed or cited.”
- “The visibility endpoint replaces Search Console or production analytics.”

## Specialist review checklist

Before production adoption, an SEO specialist should review:

1. Whether page copy contains original evidence, clear authorship, sources,
   freshness signals, and useful internal links.
2. Whether structured data matches the page’s visible claims and eligibility
   for the intended Google search feature.
3. Whether canonical, hreflang, noindex, robots, sitemap, and preview controls
   agree across author, publish, and CDN layers.
4. Whether performance, accessibility, mobile rendering, and link discovery
   are tested on the real published site.
5. Whether Search Console, analytics, and AI citation monitoring are connected
   before making business-impact claims.

## Next product-worthy additions

- A production analytics adapter with a documented provider interface.
- Search Console import/reporting behind explicit credentials and permissions.
- Persistent audit history for contract warnings and remediation status.
- Editorial checks for evidence, sources, freshness, and author identity.
- Live publish-environment smoke tests for canonical URLs, robots, sitemap,
  and rendered metadata.
