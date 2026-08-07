# ADR-002: Emit standard robots preview controls from the existing SEO component

Status: Accepted
Date: 2026-08-06

## Decision

Preview controls will be authored alongside `noIndex` and `noFollow` in the
existing SEO Metadata component and emitted in the existing `robots` meta tag.
The project will not create a second AI-specific metadata component.

## Context

Google documents `nosnippet`, `data-nosnippet`, `max-snippet`, and `noindex` as
controls for limiting previews in Search and AI features. These directives are
standard page controls, not proprietary AI hints.

## Rules

- `noSnippet=true` emits `nosnippet`.
- `maxSnippet >= 0` emits `max-snippet:<value>`.
- If both are authored, `nosnippet` takes precedence and the validator warns
  about the redundant `maxSnippet` value.
- Defaults remain unchanged for existing pages.
