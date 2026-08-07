#!/usr/bin/env bash
set -euo pipefail

base_url="${AEM_BASE_URL:-http://localhost:4502}"
demo_path="${AEM_DEMO_PATH:-/content/awesome-aem-geo/us/en}"
user_name="${AEM_USER:-admin}"
password="${AEM_PASSWORD:-admin}"
base_url="${base_url%/}"

tmp_dir="$(mktemp -d)"
trap 'rm -rf "$tmp_dir"' EXIT

request() {
  local name="$1" url="$2" expected="$3"
  local body="$tmp_dir/$name"
  local status
  status="$(curl -sS -u "$user_name:$password" -o "$body" -w '%{http_code}' "$url")"
  if [[ "$status" != "$expected" ]]; then
    echo "FAIL $name: expected HTTP $expected, got $status" >&2
    sed -n '1,8p' "$body" >&2
    exit 1
  fi
  echo "PASS $name (HTTP $status)"
}

echo "Verifying Awesome AEM GEO demo at $base_url"
request contract "$base_url/bin/awesome-aem-geo/contract.json?path=$demo_path" 200
request content "$base_url/bin/awesome-aem-geo/content.json?path=$demo_path" 200
robots_status="$(curl -sS -u "$user_name:$password" -o "$tmp_dir/robots" -w '%{http_code}' "$base_url/robots.txt")"
if [[ "$robots_status" == "200" ]]; then
  echo "PASS robots (HTTP 200)"
elif [[ "$robots_status" == "302" ]]; then
  echo "WARN robots.txt normalized by local SDK; verifying compatibility path"
  request robots "$base_url/bin/awesome-aem-geo/robots.txt" 200
else
  echo "FAIL robots: expected HTTP 200 or local SDK redirect, got $robots_status" >&2
  sed -n '1,8p' "$tmp_dir/robots" >&2
  exit 1
fi
sitemap_status="$(curl -sS -u "$user_name:$password" -o "$tmp_dir/sitemap" -w '%{http_code}' "$base_url/sitemap.xml?root=$demo_path&max=100")"
if [[ "$sitemap_status" == "200" ]]; then
  echo "PASS sitemap (HTTP 200)"
elif [[ "$sitemap_status" == "302" ]]; then
  echo "WARN sitemap.xml normalized by local SDK; verifying compatibility path"
  request sitemap "$base_url/bin/awesome-aem-geo/sitemap.xml?root=$demo_path&max=100" 200
else
  echo "FAIL sitemap: expected HTTP 200 or local SDK redirect, got $sitemap_status" >&2
  sed -n '1,8p' "$tmp_dir/sitemap" >&2
  exit 1
fi
request visibility "$base_url/bin/awesome-aem-geo/visibility.json" 200

grep -q '"valid":true' "$tmp_dir/contract" || { echo 'FAIL contract is not valid' >&2; exit 1; }
grep -q '"schema"' "$tmp_dir/content" || { echo 'FAIL content export has no schema' >&2; exit 1; }
grep -q 'Sitemap:' "$tmp_dir/robots" || { echo 'FAIL robots.txt has no sitemap reference' >&2; exit 1; }
grep -q '<urlset' "$tmp_dir/sitemap" || { echo 'FAIL sitemap is not an XML urlset' >&2; exit 1; }
grep -q 'crawlerVisits' "$tmp_dir/visibility" || { echo 'FAIL visibility report has no crawler data' >&2; exit 1; }

echo 'Demo verification passed: contract, export, robots, sitemap, and visibility are responding.'
