# Changelog

## Unreleased

- Aligned the Maven reactor with Adobe AEM Project Archetype conventions by
  adding `ui.apps.structure`, standardizing application/content install roots,
  and using the `autoInstallSinglePackage` deployment profile.

- Added deployable runtime endpoints and an all-in-one content package.
- Added open-source governance documentation and CI validation.
- Added explicit OAI-SearchBot, Claude-SearchBot, and Claude-User crawler profiles.
- Added SEO preview controls for `nosnippet` and `max-snippet`.
- Added contract warnings for visible-title/schema parity and conflicting preview controls.
- Added bounded ChatGPT referral and crawler visibility diagnostics.
- Added implementation specifications and ADRs for AI discovery boundaries.
- Added endpoint-level tests for machine-readable content and contract reports.
- Extended the browser showcase with a GEO readiness and visibility view.
- Added SEO/GEO positioning guidance and an evidence boundary for public claims.
- Documented the boundary between this project and AEM Cloud SEO/URL Management.
- Added an initial-HTML smoke test for LLM-readable SEO/GEO signals.
- Fixed the local auto-install profile to deploy the `all` container package.
- Fixed the application package to embed and install the core OSGi bundle.
- Fixed the core JAR to include its generated OSGi runtime manifest.
