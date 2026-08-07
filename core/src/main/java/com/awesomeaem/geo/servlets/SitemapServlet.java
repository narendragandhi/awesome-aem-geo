package com.awesomeaem.geo.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.awesomeaem.geo.services.SitemapGeneratorService;

@Component(service = Servlet.class, property = {
    "sling.servlet.paths=/sitemap.xml",
    "sling.servlet.paths=/bin/awesome-aem-geo/sitemap.xml",
    "sling.servlet.methods=GET"
})
public final class SitemapServlet extends SlingSafeMethodsServlet {

    private static final int MAX_URLS = 50000;

    @Reference
    private SitemapGeneratorService sitemapGeneratorService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        String root = request.getParameter("root");
        int maxUrls = parseMaxUrls(request.getParameter("max"));
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(sitemapGeneratorService.generateSitemap(root, maxUrls));
    }

    private int parseMaxUrls(String value) {
        if (value == null) {
            return MAX_URLS;
        }
        try {
            return Math.min(MAX_URLS, Math.max(1, Integer.parseInt(value)));
        } catch (NumberFormatException e) {
            return MAX_URLS;
        }
    }
}
