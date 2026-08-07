package com.awesomeaem.geo.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.awesomeaem.geo.services.ContentContractValidator;
import com.awesomeaem.geo.services.JsonLdSchemaService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Produces a small, actionable report that can be shown to authors or demo viewers.
 */
@Component(service = ContentContractValidator.class)
public final class ContentContractValidatorImpl implements ContentContractValidator {

    @Reference
    private JsonLdSchemaService jsonLdSchemaService;

    @Override
    public JsonObject validate(Resource resource) {
        JsonObject report = new JsonObject();
        JsonArray errors = new JsonArray();
        JsonArray warnings = new JsonArray();

        if (resource == null) {
            errors.add("Resource was not found");
            return finish(report, "", errors, warnings, false);
        }

        Resource content = resource.getChild("jcr:content");
        Resource contractResource = content != null ? content : resource;
        ValueMap properties = contractResource.getValueMap();
        report.addProperty("path", resource.getPath());
        report.addProperty("contractResource", contractResource.getPath());

        String title = properties.get("jcr:title", String.class);
        String description = properties.get("jcr:description", String.class);
        String canonicalUrl = properties.get("canonicalUrl", String.class);
        String schemaType = properties.get("schemaType", String.class);

        require(errors, "title", title);
        require(errors, "description", description);
        require(errors, "canonicalUrl", canonicalUrl);
        require(errors, "schemaType", schemaType);

        if (StringUtils.isNotBlank(schemaType) && !jsonLdSchemaService.isSupported(schemaType)) {
            errors.add("schemaType is not supported: " + schemaType);
        }

        String schema = jsonLdSchemaService.generateSchema(null, contractResource);
        boolean schemaValid = StringUtils.isNotBlank(schema);
        report.addProperty("schemaValid", schemaValid);
        if (schemaValid) {
            JsonObject schemaObject = JsonParser.parseString(schema).getAsJsonObject();
            report.add("schema", schemaObject);
            checkVisibleParity(warnings, title, schemaObject);
        } else {
            errors.add("Schema output is incomplete for the selected schemaType");
        }

        if (StringUtils.isBlank(properties.get("authorName", String.class))) {
            warnings.add("authorName is missing; provenance will be incomplete");
        }
        if (properties.get("publishDate") == null) {
            warnings.add("publishDate is missing; temporal provenance will be incomplete");
        }
        Boolean noSnippet = properties.get("noSnippet", Boolean.class);
        Integer maxSnippet = properties.get("maxSnippet", Integer.class);
        if (Boolean.TRUE.equals(noSnippet) && maxSnippet != null && maxSnippet >= 0) {
            warnings.add("maxSnippet is redundant because noSnippet takes precedence");
        }

        return finish(report, schemaType, errors, warnings, errors.size() == 0);
    }

    private void require(JsonArray errors, String field, String value) {
        if (StringUtils.isBlank(value)) {
            errors.add("Missing required field: " + field);
        }
    }

    private void checkVisibleParity(JsonArray warnings, String title, JsonObject schema) {
        String visibleTitle = StringUtils.defaultString(title);
        if (schema.has("headline") && !visibleTitle.equals(schema.get("headline").getAsString())) {
            warnings.add("Schema headline does not match the authored title");
        }
        if (schema.has("name") && !visibleTitle.equals(schema.get("name").getAsString())) {
            warnings.add("Schema name does not match the authored title");
        }
    }

    private JsonObject finish(JsonObject report, String schemaType, JsonArray errors,
            JsonArray warnings, boolean valid) {
        report.addProperty("schemaType", schemaType == null ? "" : schemaType);
        report.addProperty("valid", valid);
        report.add("errors", errors);
        report.add("warnings", warnings);
        return report;
    }
}
