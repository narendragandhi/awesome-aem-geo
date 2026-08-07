package com.awesomeaem.geo.servlets;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.awesomeaem.geo.services.JsonLdSchemaService;
import com.google.gson.JsonObject;

@Component(service = Servlet.class, property = {
    "sling.servlet.paths=/bin/awesome-aem-geo/content.json",
    "sling.servlet.methods=GET"
})
public final class AiContentServlet extends SlingSafeMethodsServlet {

    @Reference
    private JsonLdSchemaService jsonLdSchemaService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        String path = request.getParameter("path");
        if (StringUtils.isBlank(path) || !path.startsWith("/content/")) {
            response.sendError(SlingHttpServletResponse.SC_BAD_REQUEST,
                "path must identify a resource below /content/");
            return;
        }

        Resource resource = request.getResourceResolver().getResource(path);
        if (resource == null) {
            response.sendError(SlingHttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Resource content = resource.getChild("jcr:content");
        Resource contractResource = content != null ? content : resource;
        JsonObject result = new JsonObject();
        result.addProperty("path", resource.getPath());
        addString(result, "title", contractResource.getValueMap().get("jcr:title", String.class));
        addString(result, "description", contractResource.getValueMap().get("jcr:description", String.class));
        addDate(result, "publishedDate", contractResource.getValueMap().get("publishDate", Calendar.class));
        addDate(result, "modifiedDate", contractResource.getValueMap().get("jcr:lastModified", Calendar.class));

        String schemaJson = jsonLdSchemaService.generateSchema(null, contractResource);
        if (StringUtils.isNotBlank(schemaJson)) {
            result.add("schema", com.google.gson.JsonParser.parseString(schemaJson));
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result.toString());
    }

    private void addString(JsonObject target, String name, String value) {
        if (StringUtils.isNotBlank(value)) {
            target.addProperty(name, value);
        }
    }

    private void addDate(JsonObject target, String name, Calendar value) {
        if (value != null) {
            target.addProperty(name, value.toInstant().toString());
        }
    }
}
