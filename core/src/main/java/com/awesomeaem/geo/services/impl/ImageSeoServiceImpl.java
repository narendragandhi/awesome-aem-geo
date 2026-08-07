package com.awesomeaem.geo.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.asset.api.Asset;
import com.awesomeaem.geo.services.ImageSeoService;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(service = ImageSeoService.class)
public class ImageSeoServiceImpl implements ImageSeoService {

    private static final String SCHEMA_CONTEXT = "https://schema.org";
    private static final String IMAGE_TYPE = "ImageObject";
    private static final String SUBSERVICE_NAME = "image-seo-service";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public String generateImageSchema(Resource imageResource) {
        if (imageResource == null) {
            return "";
        }

        ValueMap properties = imageResource.getValueMap();
        if (properties == null || properties.isEmpty()) {
            return "";
        }

        Asset asset = resolveAsset(imageResource, properties);
        ValueMap metadata = getAssetMetadata(asset);

        JsonObject schema = new JsonObject();
        schema.addProperty("@context", SCHEMA_CONTEXT);
        schema.addProperty("@type", IMAGE_TYPE);

        String fileReference = properties.get("fileReference", String.class);
        if (StringUtils.isBlank(fileReference) && asset != null) {
            fileReference = asset.getPath();
        }
        if (StringUtils.isNotBlank(fileReference)) {
            schema.addProperty("url", fileReference);
            schema.addProperty("contentUrl", fileReference);
        }

        String title = firstNonBlank(
            properties.get("jcr:title", String.class),
            properties.get("title", String.class),
            metadata != null ? metadata.get("dc:title", String.class) : null,
            metadata != null ? metadata.get("jcr:title", String.class) : null
        );
        if (StringUtils.isNotBlank(title)) {
            schema.addProperty("name", title);
        }

        String description = firstNonBlank(
            properties.get("jcr:description", String.class),
            properties.get("description", String.class),
            metadata != null ? metadata.get("dc:description", String.class) : null,
            metadata != null ? metadata.get("jcr:description", String.class) : null
        );
        if (StringUtils.isNotBlank(description)) {
            schema.addProperty("description", description);
        }

        String alt = firstNonBlank(
            properties.get("alt", String.class),
            metadata != null ? metadata.get("dc:title", String.class) : null
        );
        if (StringUtils.isNotBlank(alt)) {
            schema.addProperty("alt", alt);
        }

        Object width = properties.get("width");
        if (width == null && metadata != null) {
            width = metadata.get("tiff:ImageWidth");
        }
        if (width != null) {
            schema.addProperty("width", width.toString());
        }

        Object height = properties.get("height");
        if (height == null && metadata != null) {
            height = metadata.get("tiff:ImageLength");
        }
        if (height != null) {
            schema.addProperty("height", height.toString());
        }

        String caption = firstNonBlank(
            properties.get("caption", String.class),
            metadata != null ? metadata.get("dc:description", String.class) : null
        );
        if (StringUtils.isNotBlank(caption)) {
            schema.addProperty("caption", caption);
        }

        String mimeType = firstNonBlank(
            properties.get("mimeType", properties.get("format", String.class)),
            metadata != null ? metadata.get("dc:format", String.class) : null
        );
        if (StringUtils.isNotBlank(mimeType)) {
            schema.addProperty("encodingFormat", mimeType);
        }

        String datePublished = firstNonBlank(
            properties.get("datePublished", String.class),
            properties.get("jcr:created", String.class),
            metadata != null ? metadata.get("dc:created", String.class) : null
        );
        if (StringUtils.isNotBlank(datePublished)) {
            schema.addProperty("datePublished", datePublished);
        }

        return schema.toString();
    }

    @Override
    public Map<String, Object> extractImageMetadata(Resource imageResource) {
        Map<String, Object> metadata = new HashMap<>();

        if (imageResource == null || imageResource.getValueMap() == null) {
            return metadata;
        }

        ValueMap properties = imageResource.getValueMap();
        Asset asset = resolveAsset(imageResource, properties);
        ValueMap assetMetadata = getAssetMetadata(asset);

        String title = firstNonBlank(
            properties.get("jcr:title", properties.get("title", String.class)),
            assetMetadata != null ? assetMetadata.get("dc:title", String.class) : null
        );
        if (StringUtils.isNotBlank(title)) {
            metadata.put("title", title);
        }

        String description = firstNonBlank(
            properties.get("jcr:description", properties.get("description", String.class)),
            assetMetadata != null ? assetMetadata.get("dc:description", String.class) : null
        );
        if (StringUtils.isNotBlank(description)) {
            metadata.put("description", description);
        }

        String altText = firstNonBlank(
            properties.get("alt", String.class),
            assetMetadata != null ? assetMetadata.get("dc:title", String.class) : null
        );
        if (StringUtils.isNotBlank(altText)) {
            metadata.put("altText", altText);
        }

        String caption = firstNonBlank(
            properties.get("caption", String.class),
            assetMetadata != null ? assetMetadata.get("dc:description", String.class) : null
        );
        if (StringUtils.isNotBlank(caption)) {
            metadata.put("caption", caption);
        }

        Object width = properties.get("width");
        if (width == null && assetMetadata != null) {
            width = assetMetadata.get("tiff:ImageWidth");
        }
        if (width != null) {
            metadata.put("width", width);
        }

        Object height = properties.get("height");
        if (height == null && assetMetadata != null) {
            height = assetMetadata.get("tiff:ImageLength");
        }
        if (height != null) {
            metadata.put("height", height);
        }

        String fileReference = properties.get("fileReference", String.class);
        if (StringUtils.isBlank(fileReference) && asset != null) {
            fileReference = asset.getPath();
        }
        if (StringUtils.isNotBlank(fileReference)) {
            metadata.put("url", fileReference);
        }

        return metadata;
    }

    @Override
    public boolean validateAltText(Resource imageResource) {
        if (imageResource == null) {
            return false;
        }

        ValueMap properties = imageResource.getValueMap();
        if (properties == null) {
            return false;
        }

        String alt = properties.get("alt", String.class);
        return StringUtils.isNotBlank(alt) && StringUtils.isNotBlank(alt.trim());
    }

    @Override
    public List<String> getImageSitemapEntries(String rootPath) {
        List<String> entries = new ArrayList<>();

        if (StringUtils.isBlank(rootPath)) {
            return entries;
        }

        try (ResourceResolver resolver = getServiceResolver()) {
            if (resolver == null) {
                return entries;
            }
            Resource root = resolver.getResource(rootPath);
            if (root == null) {
                return entries;
            }
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" ");
            xml.append("xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\">\n");

            for (Resource child : root.getChildren()) {
                Asset asset = child.adaptTo(Asset.class);
                if (asset == null) {
                    continue;
                }
                ValueMap meta = getAssetMetadata(asset);
                String assetPath = asset.getPath();
                String title = meta != null ? meta.get("dc:title", String.class) : null;
                xml.append("  <url>\n");
                xml.append("    <loc>").append(escapeXml(assetPath)).append("</loc>\n");
                xml.append("    <image:image>\n");
                xml.append("      <image:loc>").append(escapeXml(assetPath)).append("</image:loc>\n");
                if (StringUtils.isNotBlank(title)) {
                    xml.append("      <image:title>").append(escapeXml(title)).append("</image:title>\n");
                }
                xml.append("    </image:image>\n");
                xml.append("  </url>\n");
            }
            xml.append("</urlset>");
            entries.add(xml.toString());
        }

        return entries;
    }

    private Asset resolveAsset(Resource imageResource, ValueMap properties) {
        Asset asset = imageResource.adaptTo(Asset.class);
        if (asset != null) {
            return asset;
        }
        String fileReference = properties.get("fileReference", String.class);
        if (StringUtils.isBlank(fileReference)) {
            return null;
        }
        ResourceResolver resolver = imageResource.getResourceResolver();
        if (resolver == null) {
            return null;
        }
        Resource assetResource = resolver.getResource(fileReference);
        return assetResource != null ? assetResource.adaptTo(Asset.class) : null;
    }

    private ValueMap getAssetMetadata(Asset asset) {
        if (asset == null) {
            return null;
        }
        Resource assetResource = asset.adaptTo(Resource.class);
        if (assetResource == null) {
            return null;
        }
        Resource metadataResource = assetResource.getChild("jcr:content/metadata");
        return metadataResource != null ? metadataResource.getValueMap() : null;
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private String escapeXml(String value) {
        return value == null ? "" : value.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }

    private ResourceResolver getServiceResolver() {
        if (resourceResolverFactory == null) {
            return null;
        }
        try {
            return resourceResolverFactory.getServiceResourceResolver(
                Map.of(ResourceResolverFactory.SUBSERVICE, SUBSERVICE_NAME)
            );
        } catch (LoginException e) {
            log.warn("Unable to obtain service resolver for image sitemap", e);
            return null;
        }
    }
}
