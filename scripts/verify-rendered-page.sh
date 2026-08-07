#!/usr/bin/env bash
set -euo pipefail

base_url="${AEM_BASE_URL:-http://localhost:4502}"
page_path="${AEM_PAGE_PATH:-/content/awesome-aem-geo/us/en.html}"
user_name="${AEM_USER:-admin}"
password="${AEM_PASSWORD:-admin}"
base_url="${base_url%/}"

tmp_file="$(mktemp)"
trap 'rm -f "$tmp_file"' EXIT

url="$base_url$page_path"
status="$(curl -sS -u "$user_name:$password" -o "$tmp_file" -w '%{http_code}' "$url")"
if [[ "$status" != "200" ]]; then
  echo "FAIL rendered page: expected HTTP 200, got $status ($url)" >&2
  sed -n '1,12p' "$tmp_file" >&2
  exit 1
fi

check() {
  local label="$1" pattern="$2"
  if grep -Eqi "$pattern" "$tmp_file"; then
    echo "PASS $label"
  else
    echo "FAIL $label: initial HTML did not contain $pattern" >&2
    exit 1
  fi
}

echo "Verifying initial HTML payload at $url"
check title '<title[^>]*>'
check canonical '<link[^>]+rel=["'"']canonical["'"']'
check description '<meta[^>]+name=["'"']description["'"']'
check structured-data 'application/ld\+json'
check robots '<meta[^>]+name=["'"']robots["'"']'

echo 'Initial HTML verification passed: core SEO/GEO signals are available without client-side rendering.'
