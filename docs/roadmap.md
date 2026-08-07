# Roadmap

## Completed baseline

- Added a reproducible demo content package and browser showcase.
- Added crawler profiles, preview controls, contract quality reporting, and
  bounded referral telemetry for AI discovery diagnostics.
- Added public endpoint tests for structured content and contract responses,
  including path-safety behavior.
- Added traceable requirements and ADRs in `docs/specs/` and `docs/decisions/`.
- Added SEO/GEO positioning guidance with explicit claims, non-claims, and
  specialist review criteria.

## Next

- Add a formal content-contract model for page and Content Fragment inputs.
- Add JSON-LD validation fixtures for each supported Schema.org type.
- Add AEM integration tests for the three public endpoints and HTL output.

## Later

- Add configurable provenance vocabularies and organization policies.
- Add publish-time validation reporting for incomplete schema data.
- Add optional persistent telemetry integration behind a separate module.
- Add Search Console and analytics adapters for production visibility measurement.

## Out of scope

- Vector databases and semantic retrieval.
- LLM prompting or model hosting.
- Bot access control based on user-agent strings.
- Reimplementing general-purpose SEO functionality already provided by the site platform.
