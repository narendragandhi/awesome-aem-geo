package com.awesomeaem.geo.services;

import org.apache.sling.api.resource.Resource;

import com.google.gson.JsonObject;

/**
 * Validates the authored fields required by the machine-readable publishing contract.
 */
public interface ContentContractValidator {

    /**
     * Build a validation report for a page or content resource.
     *
     * @param resource page resource or its jcr:content resource
     * @return JSON validation report
     */
    JsonObject validate(Resource resource);
}
