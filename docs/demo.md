# Demo and showcase guide

The demo should tell one story: the same authored page becomes human-facing HTML,
validated JSON-LD, safe machine-readable content, and observable discovery signals.

## 1. Build and install

Start AEM Author on `http://localhost:4502`, then run:

```bash
mvn clean install -PautoInstallSinglePackage \
  -Daem.user=admin -Daem.password=admin
```

The install profile is explicit so ordinary builds do not contact AEM.
The profile is the standard AEM Project Archetype single-package install. It
installs the `all` container, which embeds the application package, OSGi bundle,
and reproducible demo content using Adobe's `application/install` and
`content/install` layout.

The profile then runs the idempotent `scripts/seed-demo-content.sh` bootstrap
for local SDK revisions that stage nested ZIPs without consuming them, so the
local demo remains runnable.
Cloud Manager and production deployments continue to use the archetype-standard
`all` container and authored content.

For realistic sitemap URLs, configure the AEM `publish` Externalizer domain and
the `sitemap-service` service-user mapping before testing the sitemap. Without
those runtime settings the package still installs, but the sitemap is empty or
falls back to the example domain.

## 2. Add the components to a page

Create or open a page under `/content` whose template allows the `Awesome AEM GEO` component group. Add:

- **SEO Metadata** in the page head or head Experience Fragment.
- **E-E-A-T Signals** in the page head or an Experience Fragment.

Populate the author, organization, review, provenance, title, description, canonical URL, and social image fields. View page source and confirm the meta tags and `application/ld+json` blocks.

## 3. Exercise the public endpoints

```bash
curl -u admin:admin http://localhost:4502/robots.txt
curl -u admin:admin 'http://localhost:4502/sitemap.xml?root=/content/wknd&max=100'
curl -u admin:admin \
  'http://localhost:4502/bin/awesome-aem-geo/content.json?path=/content/wknd/us/en'
curl -u admin:admin \
  'http://localhost:4502/bin/awesome-aem-geo/contract.json?path=/content/awesome-aem-geo/us/en'
```

The sitemap endpoint requires the `sitemap-service` service user mapping in AEM. If it is not configured, the endpoint returns a valid empty sitemap and logs the configuration problem.

## 4. Showcase checklist

- Page source contains canonical, Open Graph, Twitter, robots, and JSON-LD metadata.
- JSON-LD parses as JSON and uses a supported Schema.org type with required fields.
- `/robots.txt` references `/sitemap.xml`.
- Sitemap excludes `noIndex` pages and repository/system paths.
- Machine-readable content JSON contains a path, authored title/description, dates when available, and the selected schema snapshot.
- The contract report returns `valid: true`, no errors, and the emitted Article schema for the demo page.
- A request with a known AI user-agent is recorded by the filter; the user-agent is advisory and never treated as authorization.
- `/bin/awesome-aem-geo/visibility.json` separates crawler counts from ChatGPT referral counts.

## 5. Use the showcase

Open [`showcase/index.html`](../showcase/index.html) in a browser, enter the
AEM base URL and demo path, and select **Inspect page**. It presents the
contract report, machine-readable export, robots policy, sitemap, and visibility
telemetry together.

The showcase also displays bounded crawler/referral telemetry. Treat that data as
diagnostic only; it is not a replacement for Search Console or production analytics.

To verify that LLM-relevant signals are present in the initial HTML response,
run:

```bash
./scripts/verify-rendered-page.sh
```

This checks the page title, canonical link, description, robots directive, and
JSON-LD without executing browser JavaScript. Keep primary page content and
these signals in the initial response; do not assume an AI crawler will render
client-side content.

For a command-line smoke test, run:

```bash
./scripts/verify-demo.sh
```

The script fails on an unexpected HTTP response, invalid contract, missing JSON-LD,
missing sitemap reference, or invalid sitemap root.

Some local SDK revisions normalize `/robots.txt` and `/sitemap.xml` to a
trailing slash before Sling path-servlet resolution. The verifier accepts that
local redirect and checks the equivalent `/bin/awesome-aem-geo/*` compatibility
paths; production should expose the canonical routes.

For a public demo, pair this AEM instance with a small static landing page showing one authored page, its HTML source, its JSON-LD, and the endpoint responses. Do not expose an author instance or admin credentials to the internet.
