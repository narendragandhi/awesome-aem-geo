package com.awesomeaem.geo.services;

import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.adobe.cq.wcm.core.components.models.Page;

/**
 * JSON-LD Schema Service Interface
 * 
 * Provides methods to generate Schema.org JSON-LD markup for SEO and AI optimization.
 */
public interface JsonLdSchemaService {

    /**
     * Generate JSON-LD schema for a page
     * 
     * @param page the AEM page
     * @param content the page content resource
     * @return JSON-LD string or empty string if no schema
     */
    String generateSchema(Page page, Resource content);

    /**
     * Generate JSON-LD schema from page properties
     * 
     * @param properties page properties
     * @return JSON-LD string or empty string
     */
    String generateSchemaFromProperties(ValueMap properties);

    /**
     * Validate that required fields are present for a schema type
     * 
     * @param schemaType the schema type (e.g., "Article", "FAQPage")
     * @param properties the properties to validate
     * @return true if valid
     */
    boolean validateSchema(String schemaType, ValueMap properties);

    /**
     * Get set of supported schema types
     * 
     * @return set of supported schema type names
     */
    Set<String> getSupportedTypes();

    /**
     * Check if a schema type is supported
     * 
     * @param schemaType the schema type
     * @return true if supported
     */
    boolean isSupported(String schemaType);
}
