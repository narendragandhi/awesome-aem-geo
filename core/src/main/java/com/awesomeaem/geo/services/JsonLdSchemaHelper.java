package com.awesomeaem.geo.services;

import org.apache.sling.api.resource.Resource;

/**
 * Helper for JSON-LD schema in HTL templates.
 * Provides safe access to JSON-LD markup.
 */
public interface JsonLdSchemaHelper {

    /**
     * Check if schema is available for current page
     * @return true if schema exists
     */
    boolean hasSchema();

    /**
     * Get JSON-LD markup safely escaped for HTML
     * @return JSON-LD string
     */
    String getJsonLd();
}
