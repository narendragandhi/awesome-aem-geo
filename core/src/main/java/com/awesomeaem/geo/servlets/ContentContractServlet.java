package com.awesomeaem.geo.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.awesomeaem.geo.services.ContentContractValidator;

@Component(service = Servlet.class, property = {
    "sling.servlet.paths=/bin/awesome-aem-geo/contract.json",
    "sling.servlet.methods=GET"
})
public final class ContentContractServlet extends SlingSafeMethodsServlet {

    @Reference
    private ContentContractValidator validator;

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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(validator.validate(resource).toString());
    }
}
