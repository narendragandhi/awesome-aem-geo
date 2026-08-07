# ADR-001: Treat AI discovery as a crawlability and content-quality boundary

Status: Accepted
Date: 2026-08-06

## Decision

Awesome AEM GEO will provide crawl policy, preview controls, machine-readable
representations, deterministic content checks, and bounded observability. It
will not provide ranking manipulation, prompt-specific page generation, model
hosting, or user-agent authorization.

## Context

Google's current guidance says the same SEO fundamentals support AI Search and
that no special AI markup is required. OpenAI and Anthropic publish crawler
controls, but neither promises placement in answers. A product that promises
"surface in ChatGPT and Claude" would therefore overstate what the repository
can control.

## Consequences

Positive:

- The repository stays aligned with first-party platform guidance.
- Features are useful even when a particular AI system changes its ranking.
- Operators can make explicit access and preview choices.

Tradeoff:

- The project cannot promise a specific result position, citation, or answer.
- Visibility measurement requires external systems such as Search Console and
  analytics platforms.
