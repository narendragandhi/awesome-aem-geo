# Architecture and boundaries

## Product boundary

The project owns the transformation from authored AEM content to trustworthy machine-readable representations:

```text
AEM authored properties
        |
        v
validation + provenance + schema mapping
        |
        +--> page HTML metadata / JSON-LD
        +--> structured content snapshot
        +--> robots.txt / sitemap adapters
```

The project does not own vector indexing, LLM inference, bot authentication, analytics warehousing, or the site’s canonical content model.

Current platform-specific controls and their rationale are tracked in the
[AI discovery readiness specification](./specs/ai-discovery-readiness.md) and
[architecture decision records](./decisions/).

## Core versus adapters

Core behavior must be deterministic, testable, and independent of a particular crawler. Delivery adapters may use AEM request, resolver, and externalizer APIs, but must not change the meaning of the content contract.

User-agent detection is advisory telemetry only. It is deliberately not a security control because user-agent strings are forgeable.

Crawler visits and AI referrals are separate signals. The bounded visibility
endpoint is diagnostic only and must not be presented as Search Console or
production analytics.

## Extension point

New schema types should add:

1. an explicit property mapping;
2. required-field validation;
3. valid JSON-LD output tests;
4. an authoring or content-fragment contract; and
5. a demo fixture proving the end-to-end result.

If a feature cannot satisfy those criteria, it belongs in a separate integration project rather than this core.
