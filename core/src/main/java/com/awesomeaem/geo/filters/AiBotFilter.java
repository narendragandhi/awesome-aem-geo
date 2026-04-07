package com.awesomeaem.geo.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.awesomeaem.geo.services.AiBotHandlerService;

import lombok.extern.slf4j.Slf4j;

/**
 * Request filter for detecting and handling AI/LLM bots.
 * 
 * <p>Intercepts requests and identifies AI bot user-agents.</p>
 */
@Slf4j
@Component(
    service = Filter.class,
    property = {
        "sling.filter.pattern=/content/.*"
    }
)
public class AiBotFilter implements Filter {

    @Reference
    private AiBotHandlerService aiBotHandlerService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("AI Bot Filter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof SlingHttpServletRequest slingRequest && response instanceof HttpServletResponse httpResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            
            if (aiBotHandlerService != null && aiBotHandlerService.isAiBot(httpRequest)) {
                String botName = aiBotHandlerService.getBotName(httpRequest);
                String path = slingRequest.getRequestPathInfo().getResourcePath();
                
                aiBotHandlerService.recordVisit(botName, path, java.time.Instant.now());
                
                log.debug("Detected AI bot: {} accessing: {}", botName, path);
            }
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("AI Bot Filter destroyed");
    }
}
