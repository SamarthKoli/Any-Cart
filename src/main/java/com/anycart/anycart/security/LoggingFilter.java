package com.anycart.anycart.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logger.info("Incoming request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
        logger.info("Content-Type: {}", httpRequest.getContentType());
        Collections.list(httpRequest.getHeaderNames()).forEach(
            header -> logger.info("Header: {} = {}", header, httpRequest.getHeader(header))
        );

        if (httpRequest.getContentType() != null && httpRequest.getContentType().startsWith("multipart/form-data")) {
            if (httpRequest instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) httpRequest;
                multipartRequest.getParameterMap().forEach((key, value) ->
                    logger.info("Form-data part: {} = {}", key, String.join(",", value))
                );
                multipartRequest.getFileMap().forEach((key, file) ->
                    logger.info("File part: {} = {} (size: {})", key, file.getOriginalFilename(), file.getSize())
                );
            } else {
                logger.info("Multipart request not yet parsed, deferring to controller");
            }
        } else {
            logger.info("Non-multipart request, Content-Type: {}", httpRequest.getContentType());
        }

        chain.doFilter(request, response);
    }
}