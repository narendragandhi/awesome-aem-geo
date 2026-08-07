# ADR-003: Align Maven packaging with the AEM Project Archetype

- Status: Accepted
- Date: 2026-08-07
- Scope: Maven reactor and AEM content-package deployment

## Decision

Use the Adobe AEM Project Archetype module and package conventions as the
repository's build contract. The repo uses the archetype 56 structure available
from Maven Central at the time of this decision, while retaining its own
application code and documentation.

The reactor contains:

- `core`: the OSGi bundle;
- `ui.apps`: an application package containing `/apps/awesome-aem-geo`;
- `ui.apps.structure`: the repository-structure package referenced by `ui.apps`;
- `ui.content`: demo/content package containing `/content/awesome-aem-geo`;
- `all`: the single container package used for deployment.

`all` embeds the bundle and application package at
`/apps/awesome-aem-geo-packages/application/install` and content at
`/apps/awesome-aem-geo-packages/content/install`. The deployment profile is
the archetype-style `autoInstallSinglePackage` profile.

## Rationale

Adobe documents the AEM Project Archetype as the best-practice, cloud-ready
starting point for AEM projects. Matching its package boundaries makes this
repo recognizable to AEM engineers, compatible with Cloud Manager expectations,
and easier to compare with generated projects.

## Consequences

- Package type boundaries are explicit: application, content, container, and
  repository structure are not mixed.
- Ordinary Maven builds remain offline with respect to AEM.
- Local SDK installation is exercised through one `all` package.
- The explicit local profile may also install `ui.content` directly because
  some local SDK revisions do not consume nested content ZIPs; this does not
  change the production container contract.
- The Maven plugin currently targets its supported `service.jsp` endpoint. On
  newer local SDKs, use the Package Manager UI or Adobe's path-based
  `/crx/packmgr/service/.json` API for manual package installation.
- New modules should be added only when they map to an archetype responsibility
  such as `ui.config`, `it.tests`, or dispatcher configuration.

## Reference

Adobe AEM Project Archetype overview:
<https://experienceleague.adobe.com/en/docs/experience-manager-core-components/using/developing/archetype/overview>
