package com.awesomeaem.geo.services.impl;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.adobe.cq.wcm.core.components.models.Page;
import com.awesomeaem.geo.services.JsonLdSchemaHelper;
import com.awesomeaem.geo.services.JsonLdSchemaService;

/**
 * HTL Helper for JSON-LD schema rendering.
 * Provides safe access to JSON-LD markup.
 */
@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    adapters = {JsonLdSchemaHelper.class, JsonLdSchemaHelperImpl.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class JsonLdSchemaHelperImpl implements JsonLdSchemaHelper {

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @OSGiService
    private JsonLdSchemaService schemaService;

    private String jsonLd;
    private boolean hasSchema;

    @PostConstruct
    private void init() {
        if (schemaService == null) {
            this.hasSchema = false;
            this.jsonLd = null;
            return;
        }

        Resource currentResource = resource;
        Page page = null;

        if (request != null) {
            if (currentResource == null) {
                currentResource = request.getResource();
            }
            page = request.adaptTo(Page.class);
        }

        if (page == null && currentResource != null) {
            page = currentResource.adaptTo(Page.class);
        }

        if (currentResource == null && page == null) {
            this.hasSchema = false;
            this.jsonLd = null;
            return;
        }

        String schema = schemaService.generateSchema(page, currentResource);
        String safeSchema = escapeForScriptContext(schema);

        this.hasSchema = StringUtils.isNotBlank(safeSchema);
        this.jsonLd = safeSchema;
    }

    private String escapeForScriptContext(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }
        return json.replace("<", "\\u003c")
            .replace(">", "\\u003e")
            .replace("&", "\\u0026")
            .replace("\u2028", "\\u2028")
            .replace("\u2029", "\\u2029");
    }

    @Override
    public boolean hasSchema() {
        return hasSchema;
    }

    @Override
    public String getJsonLd() {
        return jsonLd;
    }
}
