package com.example.demo.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing LoggingFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest httpRequest) {
            logger.info("Incoming request");

            httpRequest.getHeaderNames().asIterator().forEachRemaining(headerName ->
                logger.info("Header: {} = {}", headerName, httpRequest.getHeader(headerName))
            );
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("Destroying LoggingFilter...");
    }
}
