package com.qlhs.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String method = req.getMethod();
            String uri = req.getRequestURI();
            int status = res.getStatus();

            // Log format: [METHOD] URI - Status: XXX - Time: XXms
            String logMessage = String.format("[%s] %s - Status: %d - Time: %dms", method, uri, status, duration);

            if (status >= 400 && status < 500) {
                logger.warn(logMessage);
            } else if (status >= 500) {
                logger.error(logMessage);
            } else {
                logger.info(logMessage);
            }
        }
    }
}
