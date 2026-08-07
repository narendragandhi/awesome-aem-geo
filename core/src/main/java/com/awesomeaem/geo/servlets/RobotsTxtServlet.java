package com.awesomeaem.geo.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.awesomeaem.geo.services.RobotsTxtService;

@Component(service = Servlet.class, property = {
    "sling.servlet.paths=/robots.txt",
    "sling.servlet.paths=/robots.txt/",
    "sling.servlet.paths=/bin/awesome-aem-geo/robots.txt",
    "sling.servlet.methods=GET"
})
public final class RobotsTxtServlet extends SlingSafeMethodsServlet {

    @Reference
    private RobotsTxtService robotsTxtService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(robotsTxtService.generateRobotsTxt(getOrigin(request)));
    }

    private String getOrigin(SlingHttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
            || ("https".equalsIgnoreCase(scheme) && port == 443);
        return scheme + "://" + host + (defaultPort ? "" : ":" + port);
    }
}
