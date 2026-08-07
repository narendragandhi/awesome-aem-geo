# AI Discovery Readiness Specification

Status: Implemented baseline; production integrations remain roadmap
Owner: Awesome AEM GEO maintainers
Last reviewed: 2026-08-06

## Purpose

Extend the publishing contract so an AEM site can make deliberate, observable
choices about Google AI Search, ChatGPT Search, and Claude web retrieval without
claiming guaranteed ranking, citation, or answer placement.

## Evidence and constraints

- Google states that AI Overviews and AI Mode use existing SEO fundamentals and
  have no additional AI-only technical requirements.
- Google identifies crawlability, indexability, visible text, internal links,
  page experience, relevant media, and structured data matching visible content
  as useful fundamentals.
- Google identifies `nosnippet`, `data-nosnippet`, `max-snippet`, and `noindex`
  as preview/access controls.
- OpenAI identifies `OAI-SearchBot` as the crawler relevant to ChatGPT Search
  discovery and states that inclusion or ranking is not guaranteed.
- Anthropic distinguishes `Claude-SearchBot` and `Claude-User` for search and
  user-directed retrieval from `ClaudeBot` for model-development crawling.
- Google does not require `llms.txt` or a special AI schema format.

Sources:

- https://developers.google.com/search/docs/appearance/ai-features
- https://developers.google.com/search/updates
- https://help.openai.com/en/articles/12627856-publishers-and-developers-faq
- https://support.claude.com/en/articles/8896518-does-anthropic-crawl-data-from-the-web-and-how-can-site-owners-block-the-crawler

## Functional requirements

### DISC-001 — Crawler profile coverage

The default crawler policy MUST include explicit rules for `OAI-SearchBot`,
`Claude-SearchBot`, and `Claude-User`. `ClaudeBot` MUST remain separately
identifiable because it represents model-development crawling rather than
user-directed search retrieval.

### DISC-002 — Preview controls

The SEO metadata contract MUST support `noSnippet` and non-negative
`maxSnippet` values. The emitted robots meta directive MUST preserve the current
`index`/`follow` behavior and append only configured preview controls.

### DISC-003 — Contract quality report

The contract validation report MUST expose deterministic checks for:

- title, description, canonical URL, schema type, and schema output;
- author and publication date provenance;
- visible-content and structured-data parity when the page resource is available;
- crawlability and preview-control conflicts.

Warnings MUST be distinguishable from errors. Validation MUST NOT claim that a
valid report guarantees search or answer inclusion.

### DISC-004 — Referral and crawler observability

Observability MUST distinguish crawler visits from user referrals. It MUST
recognize `utm_source=chatgpt.com` as a ChatGPT referral signal and retain the
existing bounded in-memory behavior as the default.

### DISC-005 — Traceability

Each implemented requirement MUST have at least one automated test and one
documentation reference. Design tradeoffs MUST be recorded in an ADR.

## Non-goals

- Guaranteeing Google ranking, AI Overview inclusion, ChatGPT citation, or Claude citation.
- Adding `llms.txt` as a ranking feature.
- Blocking or authorizing requests based on user-agent strings.
- Replacing Search Console, Analytics, Merchant Center, or Business Profile.
- Treating FAQ or HowTo JSON-LD as a promise of Google rich-result eligibility.

## Acceptance criteria

1. `robots.txt` contains the three current search/retrieval agents and keeps
   training crawlers separately visible.
2. SEO metadata tests cover default, enabled, inherited, and conflicting
   preview controls.
3. A demo contract report shows errors, warnings, schema output, and a clear
   non-guarantee message.
4. Referral tests recognize ChatGPT referral URLs without misclassifying normal
   crawler visits.
5. `mvn verify` passes and the README/demo guide link to this specification.
