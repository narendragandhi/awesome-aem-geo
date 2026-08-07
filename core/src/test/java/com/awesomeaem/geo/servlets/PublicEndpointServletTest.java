package com.awesomeaem.geo.servlets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.Test;

import com.awesomeaem.geo.services.ContentContractValidator;
import com.awesomeaem.geo.services.JsonLdSchemaService;
import com.google.gson.JsonObject;

class PublicEndpointServletTest {

    @Test
    void contentEndpointReturnsSafeMachineReadablePayload() throws Exception {
        Resource page = mock(Resource.class);
        Resource content = mock(Resource.class);
        when(page.getPath()).thenReturn("/content/demo");
        when(page.getChild("jcr:content")).thenReturn(content);
        when(content.getValueMap()).thenReturn(new ValueMapDecorator(Map.of(
            "jcr:title", "Demo page",
            "jcr:description", "A useful demo"
        )));

        JsonLdSchemaService schemaService = mock(JsonLdSchemaService.class);
        when(schemaService.generateSchema(null, content)).thenReturn(
            "{\"@type\":\"Article\",\"headline\":\"Demo page\"}");
        AiContentServlet servlet = new AiContentServlet();
        inject(servlet, "jsonLdSchemaService", schemaService);

        SlingHttpServletRequest request = request(page, "/content/demo");
        ResponseFixture fixture = response();
        servlet.doGet(request, fixture.response());

        String body = fixture.body().toString();
        assertTrue(body.contains("\"title\":\"Demo page\""));
        assertTrue(body.contains("\"schema\""));
        verify(fixture.response()).setContentType("application/json");
    }

    @Test
    void contractEndpointReturnsValidationReport() throws Exception {
        Resource page = mock(Resource.class);
        when(page.getPath()).thenReturn("/content/demo");
        JsonObject report = new JsonObject();
        report.addProperty("valid", true);

        ContentContractValidator validator = mock(ContentContractValidator.class);
        when(validator.validate(page)).thenReturn(report);
        ContentContractServlet servlet = new ContentContractServlet();
        inject(servlet, "validator", validator);

        SlingHttpServletRequest request = request(page, "/content/demo");
        ResponseFixture fixture = response();
        servlet.doGet(request, fixture.response());

        assertTrue(fixture.body().toString().contains("\"valid\":true"));
        verify(fixture.response()).setCharacterEncoding("UTF-8");
    }

    @Test
    void publicEndpointsRejectPathsOutsideContent() throws Exception {
        ContentContractServlet servlet = new ContentContractServlet();
        SlingHttpServletRequest request = request(null, "/apps/system");
        ResponseFixture fixture = response();

        servlet.doGet(request, fixture.response());

        verify(fixture.response()).sendError(HttpServletResponse.SC_BAD_REQUEST,
            "path must identify a resource below /content/");
    }

    private SlingHttpServletRequest request(Resource resource, String path) {
        SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        ResourceResolver resolver = mock(ResourceResolver.class);
        when(request.getParameter("path")).thenReturn(path);
        when(request.getResourceResolver()).thenReturn(resolver);
        when(resolver.getResource(path)).thenReturn(resource);
        return request;
    }

    private ResponseFixture response() throws Exception {
        SlingHttpServletResponse response = mock(SlingHttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        return new ResponseFixture(response, body);
    }

    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private record ResponseFixture(SlingHttpServletResponse response, StringWriter body) {
    }
}
