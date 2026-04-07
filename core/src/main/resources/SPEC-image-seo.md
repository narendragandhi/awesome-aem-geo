# Image SEO Feature Specification

## Overview
Add image SEO capabilities to Awesome AEM GEO for optimizing images for search engines and AI crawlers.

## Goals
- Generate ImageObject schema markup for SEO
- Validate and enforce alt text on images
- Support image sitemaps for Google Images
- Provide image metadata extraction for AI content understanding

## Components

### 1. ImageSeoService
Service interface for image SEO operations.

**Methods:**
- `generateImageSchema(Resource imageResource)` - Generate ImageObject JSON-LD
- `extractImageMetadata(Resource imageResource)` - Extract SEO-relevant metadata
- `validateAltText(Resource imageResource)` - Validate alt text presence
- `getImageSitemapEntries(String rootPath)` - Get entries for image sitemap

### 2. ImageSeoModel
Sling Model for image component SEO data.

**Exported Properties:**
- `@Self` image resource
- `imageUrl` - Full URL to image
- `altText` - Alt text for accessibility/SEO
- `title` - Image title
- `description` - Image description
- `caption` - Caption text
- `width`, `height` - Dimensions
- `imageSchema` - Generated JSON-LD schema
- `hasValidAltText` - Boolean validation flag

## Schema: ImageObject
```json
{
  "@context": "https://schema.org",
  "@type": "ImageObject",
  "url": "https://example.com/image.jpg",
  "name": "Image Title",
  "description": "Image description",
  "width": "800",
  "height": "600",
  "caption": "Image caption",
  "contentUrl": "https://example.com/image.jpg",
  "encodingFormat": "image/jpeg",
  "datePublished": "2024-01-15"
}
```

## Acceptance Criteria
1. Service generates valid ImageObject JSON-LD schema
2. Alt text validation returns true for images with alt text
3. Alt text validation returns false for missing alt text with warning
4. Image sitemap entries include required ImageObject properties
5. Model exports all required properties for Sling exporter
6. Unit tests achieve >80% coverage on new code

## Dependencies
- AEM Core Components Image (optional)
- Gson for JSON generation
- Existing JsonLdSchemaService for base schema utilities
