#!/usr/bin/env bash
set -euo pipefail

base_url="${AEM_BASE_URL:-http://localhost:4502}"
user="${AEM_USER:-admin}"
password="${AEM_PASSWORD:-admin}"
page_path="${AEM_DEMO_PATH:-/content/awesome-aem-geo/us/en}"

if [[ "$(curl -sS -u "$user:$password" -o /dev/null -w '%{http_code}' "$base_url$page_path.json")" == "200" ]]; then
  echo "Demo content already exists at $page_path"
  exit 0
fi

echo "Seeding local AEM demo content at $page_path"
curl -sS -u "$user:$password" \
  -F 'jcr:primaryType=cq:Folder' \
  -F 'jcr:title=Awesome AEM GEO Demo' \
  "$base_url/content/awesome-aem-geo" >/dev/null

curl -sS -u "$user:$password" \
  -F 'jcr:primaryType=cq:Folder' \
  -F 'jcr:title=United States' \
  "$base_url/content/awesome-aem-geo/us" >/dev/null

curl -sS -u "$user:$password" \
  -F 'jcr:primaryType=cq:Page' \
  -F 'jcr:content/jcr:primaryType=nt:unstructured' \
  -F 'jcr:content/jcr:title=Machine-readable publishing with AEM' \
  -F 'jcr:content/jcr:description=A reproducible demonstration of one authored AEM page becoming trustworthy metadata for people, crawlers, and machine consumers.' \
  -F 'jcr:content/schemaType=Article' \
  -F 'jcr:content/authorName=Awesome AEM GEO Editorial Team' \
  -F 'jcr:content/canonicalUrl=https://www.example.com/content/awesome-aem-geo/us/en.html' \
  -F 'jcr:content/publishDate=2026-08-05T12:00:00.000Z' \
  -F 'jcr:content/jcr:lastModified=2026-08-05T12:00:00.000Z' \
  -F 'jcr:content/cq:template=/conf/awesome-aem-geo/settings/wcm/templates/demo' \
  "$base_url$page_path" >/dev/null

echo "Demo content seeded"
