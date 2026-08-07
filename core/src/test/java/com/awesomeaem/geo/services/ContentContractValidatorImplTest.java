package com.awesomeaem.geo.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.Test;

import com.awesomeaem.geo.services.impl.ContentContractValidatorImpl;
import com.google.gson.JsonParser;

class ContentContractValidatorImplTest {

    @Test
    void reportsValidContractAndEmittedSchema() throws Exception {
        Resource page = mock(Resource.class);
        Resource content = mock(Resource.class);
        when(page.getPath()).thenReturn("/content/demo");
        when(content.getPath()).thenReturn("/content/demo/jcr:content");
        when(page.getChild("jcr:content")).thenReturn(content);
        when(content.getValueMap()).thenReturn(new ValueMapDecorator(Map.of(
            "jcr:title", "Demo page",
            "jcr:description", "A useful demo",
            "canonicalUrl", "https://example.com/demo",
            "schemaType", "Article",
            "authorName", "Demo author",
            "publishDate", "2026-08-05"
        )));

        JsonLdSchemaService schemaService = mock(JsonLdSchemaService.class);
        when(schemaService.isSupported("Article")).thenReturn(true);
        when(schemaService.generateSchema(null, content)).thenReturn(
            "{\"@context\":\"https://schema.org\",\"@type\":\"Article\"}");

        ContentContractValidatorImpl validator = new ContentContractValidatorImpl();
        inject(validator, "jsonLdSchemaService", schemaService);

        var report = validator.validate(page);

        assertTrue(report.get("valid").getAsBoolean());
        assertTrue(report.get("schemaValid").getAsBoolean());
        assertTrue(report.get("schema").equals(JsonParser.parseString(
            "{\"@context\":\"https://schema.org\",\"@type\":\"Article\"}")));
    }

    @Test
    void reportsMissingResourceAsInvalid() {
        ContentContractValidatorImpl validator = new ContentContractValidatorImpl();

        var report = validator.validate(null);

        assertFalse(report.get("valid").getAsBoolean());
        assertTrue(report.getAsJsonArray("errors").size() > 0);
    }

    @Test
    void reportsVisibleSchemaMismatchAsWarning() throws Exception {
        Resource page = mock(Resource.class);
        Resource content = mock(Resource.class);
        when(page.getPath()).thenReturn("/content/demo");
        when(content.getPath()).thenReturn("/content/demo/jcr:content");
        when(page.getChild("jcr:content")).thenReturn(content);
        when(content.getValueMap()).thenReturn(new ValueMapDecorator(Map.of(
            "jcr:title", "Authored title",
            "jcr:description", "A useful demo",
            "canonicalUrl", "https://example.com/demo",
            "schemaType", "Article"
        )));

        JsonLdSchemaService schemaService = mock(JsonLdSchemaService.class);
        when(schemaService.isSupported("Article")).thenReturn(true);
        when(schemaService.generateSchema(null, content)).thenReturn(
            "{\"@type\":\"Article\",\"headline\":\"Different title\"}");

        ContentContractValidatorImpl validator = new ContentContractValidatorImpl();
        inject(validator, "jsonLdSchemaService", schemaService);

        var report = validator.validate(page);

        assertTrue(report.get("valid").getAsBoolean());
        assertTrue(report.getAsJsonArray("warnings").toString().contains(
            "Schema headline does not match the authored title"));
    }

    @Test
    void reportsConflictingPreviewControlsAsWarning() throws Exception {
        Resource page = mock(Resource.class);
        Resource content = mock(Resource.class);
        when(page.getPath()).thenReturn("/content/demo");
        when(content.getPath()).thenReturn("/content/demo/jcr:content");
        when(page.getChild("jcr:content")).thenReturn(content);
        when(content.getValueMap()).thenReturn(new ValueMapDecorator(Map.of(
            "jcr:title", "Demo page",
            "jcr:description", "A useful demo",
            "canonicalUrl", "https://example.com/demo",
            "schemaType", "Article",
            "noSnippet", true,
            "maxSnippet", 120
        )));

        JsonLdSchemaService schemaService = mock(JsonLdSchemaService.class);
        when(schemaService.isSupported("Article")).thenReturn(true);
        when(schemaService.generateSchema(null, content)).thenReturn(
            "{\"@type\":\"Article\",\"headline\":\"Demo page\"}");

        ContentContractValidatorImpl validator = new ContentContractValidatorImpl();
        inject(validator, "jsonLdSchemaService", schemaService);

        var report = validator.validate(page);

        assertTrue(report.get("valid").getAsBoolean());
        assertTrue(report.getAsJsonArray("warnings").toString().contains(
            "maxSnippet is redundant because noSnippet takes precedence"));
    }

    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
