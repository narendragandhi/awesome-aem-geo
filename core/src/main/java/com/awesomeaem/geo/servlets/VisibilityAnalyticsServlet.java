package com.awesomeaem.geo.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.awesomeaem.geo.services.AiAnalyticsService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Bounded development visibility summary for crawler and referral signals.
 */
@Component(service = Servlet.class, property = {
    "sling.servlet.paths=/bin/awesome-aem-geo/visibility.json",
    "sling.servlet.methods=GET"
})
public final class VisibilityAnalyticsServlet extends SlingSafeMethodsServlet {

    private static final Gson GSON = new Gson();

    @Reference
    private AiAnalyticsService analyticsService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        JsonObject result = new JsonObject();
        result.add("crawlerVisits", GSON.toJsonTree(analyticsService.getBotBreakdown()));
        result.add("aiReferrals", GSON.toJsonTree(analyticsService.getReferralBreakdown()));
        result.addProperty("disclaimer", "Development telemetry; not Search Console or production analytics.");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result.toString());
    }
}
